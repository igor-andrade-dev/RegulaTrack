import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({ selector:'app-forgot-password', standalone:true, imports:[FormsModule, RouterLink], templateUrl:'./forgot-password.html', styleUrl:'./forgot-password.scss' })
export class ForgotPassword {
  email=''; loading=false; submitted=false; errorMessage='';
  private readonly auth=inject(AuthService);
  submit():void { if(this.loading || !this.email.trim()) return; this.loading=true; this.errorMessage=''; this.auth.requestPasswordReset(this.email.trim()).subscribe({next:()=>{this.loading=false;this.submitted=true;},error:err=>{this.loading=false;this.errorMessage=err?.error?.message||'Unable to process the request.';}}); }
}
