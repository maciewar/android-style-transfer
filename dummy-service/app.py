# rest.py
import base64
import tempfile

from flask import Flask
from flask_restful import Resource, Api, reqparse

APP = Flask(__name__)
API = Api(APP)

parser = reqparse.RequestParser()
parser.add_argument('image', type=str)
parser.add_argument('style', type=int)

class Style(Resource):
    def post(self):
        request = parser.parse_args()
        image_data = base64.urlsafe_b64decode(request.image)

        with tempfile.NamedTemporaryFile() as temp_file:
            temp_file.write(image_data)

        #encoded_image = base64.b64encode(temp_file.read()).decode('UTF-8')

        print('Saved to {}'.format(temp_file))

        return {'result': 'OK'}

API.add_resource(Style, '/convert')

if __name__ == '__main__':
    APP.run(host='0.0.0.0', debug=True)

