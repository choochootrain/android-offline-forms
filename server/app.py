#simple webapp to listen for requests from app
from flask import Flask, render_template, request

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

        return "success"
    else:
        return "error"

if __name__ == "__main__":
    app.run(debug=True, port=5000)
