#simple webapp to listen for requests from app
from flask import Flask, render_template, request
import time

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/form', methods=['POST'])
def form_listener():
    if request.form:
        print
        print "Recieved formdata"
        for key in request.form.keys():
            print "%s: %s" % (key, request.form[key])
        print 

    if request.files:
        print
        print "Recieved files"
        for key in request.files.keys():
            print "%s-%s saved" % (int(time.time()), key)
            request.files[key].save("%s-%s" % (int(time.time()), key))
        print

        return "success"
    else:
        return "error"

if __name__ == "__main__":
    app.run(host='0.0.0.0', debug=True, port=5000)
