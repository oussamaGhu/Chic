import { Injectable } from '@nestjs/common';
import * as nodemailer from 'nodemailer';

@Injectable()
export class MailService {
  private transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: process.env.EMAIL_USER, // Votre adresse Gmail (ex: votre-email@gmail.com)
      pass: process.env.EMAIL_PASS, // Mot de passe d'application ou mot de passe réel
    },
  });

  // Fonction pour envoyer le lien de réinitialisation
  async sendResetPasswordEmail(userEmail: string, token: string) {
    const resetUrl = `${process.env.BASE_URL}/auth/new-password/${token}`;

    await this.transporter.sendMail({
      from: `"Votre Application" <${process.env.EMAIL_USER}>`,
      to: userEmail, // L'adresse e-mail de l'utilisateur
      subject: 'Réinitialisation de votre mot de passe',
      html: `
        <h3>Réinitialiser votre mot de passe</h3>
        <p>Cliquez sur le lien ci-dessous pour réinitialiser votre mot de passe :</p>
        <a href="${resetUrl}">Réinitialiser mon mot de passe</a>
        <p>Si le lien ne fonctionne pas, copiez et collez ce lien dans votre navigateur :</p>
        <p style="font-family: monospace;">${resetUrl}</p>
        <button id="openAppButton" style="padding: 10px 20px; font-size: 16px;">Open App</button>

  <script>
    document.getElementById("openAppButton").addEventListener("click", function () {
      // Attempt to open the app with the custom URL scheme
      window.location.href = ${resetUrl};
    });
  </script>
        `,
    });
  }
}
































































/*import * as nodemailer from 'nodemailer';
import { Injectable } from '@nestjs/common';

@Injectable()
export class MailService {
  private transporter: nodemailer.Transporter;

  constructor() {
    this.transporter = nodemailer.createTransport({
      host: 'smtp.ethereal.email',
      port: 587,
      auth: {
        user: 'blake.herzog94@ethereal.email',
        pass: 'NqP593h9Cd7Sx9NVQP',
      },
    });
  }

  async sendPasswordResetEmail(to: string, token: string) {
    try {
      const resetLink = `http://chicCircle.com/reset-password?token=${token}`;
      const mailOptions = {
        from: 'Auth-backend service',
        to: to,
        subject: 'Password Reset Request',
        html: `<p>You requested a password reset. Click the link below to reset your password:</p><p><a href="${resetLink}">Reset Password</a></p>`,
      };

      const info = await this.transporter.sendMail(mailOptions);
      console.log('Email sent:', info);
    } catch (error) {
      console.error('Error sending email:', error);
    }
  }
}
*/