import { Body, Controller,Get, Post, Put, Req,Res,Param, UploadedFile, UseGuards, UseInterceptors } from '@nestjs/common';
import { AuthService } from './auth.service';
import { signUpDto } from './dto/signUp.dto';
import { LoginDto } from './dto/login.dto';
import { RefreshTokenDto } from './dto/refreshToken.dto';
import { AuthenticationGuard } from './Guard/guard';
import { ChangePasswordDto } from './dto/changePassword';
import { ForgotPasswordDto } from './dto/forgotPasswordDto';
import { ResetPasswordDto } from './dto/resetPassword';
import { join } from 'path';
import { Response } from 'express';

@Controller('auth')
export class AuthController {
    constructor(private readonly authService: AuthService) {}

     @Post('signUp')
     async signUp (@Body () signupData :signUpDto) {
      
        return this.authService.signup(signupData);
     }

     @Post('google')
     async googleAccess (@Body () signupData :signUpDto) {
      
        return this.authService.googleAccess(signupData);
     }

  
     @Post('login')
     async login (@Body ()Credentials  :LoginDto){
        return this.authService.login(Credentials );

     }
     @Post('refresh')
     async refreshTokens(@Body() refreshTokenDto: RefreshTokenDto) {
      return this.authService.refreshTokens(refreshTokenDto.refreshToken);
     }
     
  @UseGuards(AuthenticationGuard)
  @Put('change-password')
  async changePassword(
    @Body() changePasswordDto: ChangePasswordDto,
    @Req() req,
  ) {
    return this.authService.changePassword(
      req.userId,
      changePasswordDto.oldPassword,
      changePasswordDto.newPassword,
    );
  }
  @Post('forgot-password')
  async forgotPassword(@Body() forgotPasswordDto: ForgotPasswordDto) {
    return this.authService.forgotPassword(forgotPasswordDto.email);
  }
  @Put('reset-password')
  async resetPassword(
    @Body() resetPasswordDto: ResetPasswordDto,
  ) {
    return this.authService.resetPassword(
      resetPasswordDto.newPassword,
      resetPasswordDto.resetToken,
    );
  }

  
  @Get('new-password/:token')
  async getNewPasswordInterface(
    @Param('token') token: string,
    @Res() res: Response,
  ) {
    const htmlContent = `
      <!DOCTYPE html>
      <html lang="en">
      <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password</title>
        <style>
          body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
          }
          .container {
            background: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
          }
          input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
          }
          button {
            width: 100%;
            padding: 10px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
          }
          button:hover {
            background-color: #218838;
          }
        </style>
        <script>
  async function resetPassword(event) {
    event.preventDefault();
    const token = document.querySelector('input[name="token"]').value;
    const newPassword = document.querySelector('input[name="password"]').value;
    const confirmPassword = document.querySelector('input[name="confirm-password"]').value;

    console.log('Token:', token);
    console.log('New Password:', newPassword);

    if (newPassword !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    const response = await fetch('/auth/reset-password', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        resetToken: token,
        newPassword: newPassword,
      }),
    });

    if (response.ok) {
      alert('Password reset successfully');
    } else {
      const error = await response.json();
      console.error('Failed to reset password:', error);
      alert('Failed to reset password: ' + error.message);
    }
  }
</script>
      </head>
      <body>
        <div class="container">
          <h2>Reset Your Password</h2>
          <form onsubmit="resetPassword(event)">
            <input type="hidden" name="token" value="${token}">
            <label for="password">New Password</label>
            <input type="password" id="password" name="password" required>
            <label for="confirm-password">Confirm Password</label>
            <input type="password" id="confirm-password" name="confirm-password" required>
            <button type="submit">Reset Password</button>
          </form>
        </div>
      </body>
      </html>
    `;

    res.setHeader('Content-Type', 'text/html');
    res.send(htmlContent);
  }
}
  



