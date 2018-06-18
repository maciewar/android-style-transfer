# rest.py
import base64
import tempfile
import time

from flask import Flask
from flask_restful import Resource, Api, reqparse

APP = Flask(__name__)
API = Api(APP)

parser = reqparse.RequestParser()
parser.add_argument('image', type=str)
parser.add_argument('style', type=str)

class Style(Resource):
    def post(self):
        request = parser.parse_args()
        image_data = base64.urlsafe_b64decode(request.image)

        encoded_image = None
        with tempfile.NamedTemporaryFile() as temp_file:
            temp_file.write(image_data)
            encoded_image = base64.urlsafe_b64encode(image_data).decode('UTF-8')

        print('Saved to {}'.format(temp_file))
        result = {'result': encoded_image}
        print(result)
        return result

API.add_resource(Style, '/convert')

if __name__ == '__main__':
    APP.run(host='0.0.0.0', debug=True)

