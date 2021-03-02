import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { RegisterDto, UserLoginDto } from '../shared/dto/user.model';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit {

  isLoginMode = true;
  error: string = null;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
  }

  onSwitchMode() {
    this.isLoginMode = !this.isLoginMode;
  }

  async onSubmit(form: NgForm) {
    if (!form.valid) {
      return;
    }

    if (this.isLoginMode) {
            const userLogin: UserLoginDto = {
        password: form.value.password,
        userId: form.value.userName,
      };
      this.authService.login(userLogin);      
    } else {
      const userRegister: RegisterDto = {
        userId: form.value.userName,
        password: form.value.password,
        name: form.value.fullName,
      };
      this.authService.register(userRegister);
      this.router.navigate(['/info']);
    }

    form.reset();
  }

}
