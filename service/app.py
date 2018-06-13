import base64
import json
import tempfile
from argparse import ArgumentParser

import numpy as np
import tensorflow as tf
from flask import Flask
from flask_restful import Resource, Api, reqparse

from magenta.models.image_stylization import image_utils
from magenta.models.image_stylization import model
from magenta.models.image_stylization.image_stylization_transform import _load_checkpoint

ARGUMENT_PARSER = ArgumentParser('app.py')
ARGUMENT_PARSER.add_argument(
    'model_path',
    help='Path to a style transfer model checkpoint.'
)
ARGUMENT_PARSER.add_argument(
    'styles_count', type=int,
    help='Number of different styles available in the provided model.'
)

ARGUMENTS = ARGUMENT_PARSER.parse_args()

APP = Flask(__name__)
API = Api(APP)

REQUEST_PARSER = reqparse.RequestParser()
REQUEST_PARSER.add_argument('image', type=str)
REQUEST_PARSER.add_argument('style', type=str)

with open('styles.json') as styles_file:
    STYLES = json.load(styles_file)


class Style(Resource):
    def post(self):
        request = REQUEST_PARSER.parse_args()
        image_data = base64.urlsafe_b64decode(request.image)

        with tempfile.NamedTemporaryFile() as temp_file:
            temp_file.write(image_data)
            image = image_utils.load_np_image(temp_file.name)
            image = np.expand_dims(image, 0)

        style_id = STYLES[request.style]
        stylized_image = stylize_image(image, style_id)

        with tempfile.NamedTemporaryFile() as temp_file:
            image_utils.save_np_image(
                stylized_image[None, ...],
                temp_file.name
            )

            encoded_image = base64.urlsafe_b64encode(temp_file.read()).decode('UTF-8')

        return {'image': encoded_image}


def stylize_image(input_image, style_id):
    with tf.Graph().as_default(), tf.Session() as sess:
        stylized_images = model.transform(
            tf.concat([input_image], 0),
            normalizer_params={
                'labels': tf.constant([style_id]),
                'num_categories': ARGUMENTS.styles_count,
                'center': True,
                'scale': True
            }
        )

        _load_checkpoint(sess, ARGUMENTS.model_path)
        stylized_images = stylized_images.eval()
        return stylized_images[0]


API.add_resource(Style, '/')

if __name__ == '__main__':
    APP.run(host='0.0.0.0', debug=True)
