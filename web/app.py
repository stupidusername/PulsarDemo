from flask import Flask, render_template, request
import os

app = Flask(__name__)

PATH = os.path.abspath(__file__)
DIR_PATH = os.path.dirname(PATH)
MESSAGES_PATH = DIR_PATH + '/runtime/messages.txt'


@app.route('/message', methods=['POST'])
def save_message():
    content = request.get_data().decode('UTF-8')
    with open(MESSAGES_PATH, 'a') as myfile:
        myfile.write(content + '\n')
    return 'Message Received.'


@app.route('/read-messages', methods=['GET'])
def read_messages():
    try:
        messages = [message.rstrip('\n') for message in open(MESSAGES_PATH)]
    except OSError:
        messages = []
    return render_template('messages.html', messages=messages)
