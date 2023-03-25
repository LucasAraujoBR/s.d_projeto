import os
from django.core.mail import EmailMessage

class Util:
    @staticmethod
    def send_email(data):
        email = EmailMessage(
            from_email=os.getenv('DEFAULT_FROM_EMAIL'),subject=data['email_subject'],to=[data['to_email']])
        email.attach(data['link'])
        email.send()