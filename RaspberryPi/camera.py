import io
import socket
import struct
import time
import picamera

client_socket = socket.socket()
client_socket.connect(('host 주소',4534))

connection = client_socket.makefile('wb')
try:
	with picamera.PiCamera() as camera:
		camera.resolution = (1280,720)
        	camera.start_preview()
       		time.sleep(2)

      		stream = io.BytesIO()
		for foo in camera.capture_continuous(stream,'jpeg'):
			connection.write(struct.pack('<L', stream.tell()))
           		connection.flush()
           		stream.seek(0)
           		connection.write(stream.read())
           		stream.seek(0)
            		stream.truncate()
   		connection.write(struct.pack('<L', 0))
finally:
	connection.close()
	client_socket.close()
