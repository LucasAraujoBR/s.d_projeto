import Pyro4
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

@Pyro4.expose
class Email(object):
    
    def send_email(self, receiver,body):
        # Configuração do e-mail
        sender = 'noreply@hotel.com'
        receiver = receiver
        subject = 'Hotel'
        body = body

        message = MIMEMultipart()
        message['From'] = sender
        message['To'] = receiver
        message['Subject'] = subject
        message.attach(MIMEText(body, 'plain'))

        # Conexão com o servidor SMTP e envio do e-mail
        with smtplib.SMTP("sandbox.smtp.mailtrap.io", 2525) as server:
            server.login("195530338e0fa0", "a6d078ddf300b0")
            server.sendmail(sender, receiver, message)

    


print('Email Server ...')
daemon = Pyro4.Daemon.serveSimple({ Email: 'Email',}, host="localhost", port=8080, ns=False, verbose=True)				
ns = Pyro4.locateNS() 					
server = Email()					
uri = daemon.register(server)			
ns.register('email_Server', uri)	
daemon.requestLoop()