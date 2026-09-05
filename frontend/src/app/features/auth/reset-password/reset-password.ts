import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth';

@Component({ selector:'app-reset-password', standalone:true, imports:[FormsModule, RouterLink], templateUrl:'./reset-password.html', styleUrl:'./reset-password.scss' })
export class ResetPassword {
  password=''; confirmPassword=''; loading=false; success=false; errorMessage=''; private readonly auth=inject(AuthService); private readonly route=inject(ActivatedRoute); private readonly router=inject(Router);
  submit():void { const token=this.route.snapshot.queryParamMap.get('token')||''; if(this.loading)return; if(!token){this.errorMessage='Invalid or expired reset link.';return;} if(this.password.length<6){this.errorMessage='Password must contain at least 6 characters.';return;} if(this.password!==this.confirmPassword){this.errorMessage='Passwords do not match.';return;} this.loading=true; this.errorMessage=''; this.auth.resetPassword(token,this.password).subscribe({next:()=>{this.loading=false;this.success=true;setTimeout(()=>this.router.navigate(['/login']),1200);},error:err=>{this.loading=false;this.errorMessage=err?.error?.message||'Invalid or expired reset link.';}}); }
}
