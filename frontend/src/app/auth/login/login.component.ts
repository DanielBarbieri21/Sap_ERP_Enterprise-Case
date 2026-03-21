import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-container">
      <div class="login-card">
        <div class="eyebrow">Enterprise Case</div>
        <h1>SAP ERP</h1>
        <p class="subtitle">
          Plataforma modular para operacoes financeiras, compras, vendas, estoque e RH.
        </p>

        <form [formGroup]="loginForm" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Email</label>
            <input type="email" formControlName="email" class="form-control" />
            <div *ngIf="loginForm.get('email')?.hasError('required') && loginForm.get('email')?.touched" class="error">
              Email e obrigatorio
            </div>
          </div>

          <div class="form-group">
            <label>Senha</label>
            <input type="password" formControlName="password" class="form-control" />
            <div *ngIf="loginForm.get('password')?.hasError('required') && loginForm.get('password')?.touched" class="error">
              Senha e obrigatoria
            </div>
          </div>

          <div *ngIf="error" class="error-message">{{ error }}</div>

          <button type="submit" [disabled]="loginForm.invalid || loading" class="btn-primary">
            {{ loading ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    .login-container {
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 100vh;
      padding: 2rem;
      background:
        radial-gradient(circle at top left, rgba(198, 146, 20, 0.22), transparent 28%),
        radial-gradient(circle at bottom right, rgba(17, 24, 39, 0.16), transparent 24%),
        linear-gradient(135deg, #111827 0%, #1f2937 45%, #4b5563 100%);
    }

    .login-card {
      background: rgba(255, 255, 255, 0.96);
      padding: 2.5rem;
      border-radius: 24px;
      box-shadow: 0 24px 80px rgba(15, 23, 42, 0.28);
      border: 1px solid rgba(255, 255, 255, 0.45);
      width: 100%;
      max-width: 460px;
    }

    .eyebrow {
      text-transform: uppercase;
      letter-spacing: 0.18em;
      font-size: 0.75rem;
      font-weight: 700;
      color: #9f6c00;
      margin-bottom: 0.75rem;
    }

    h1 {
      color: #111827;
      margin-bottom: 0.5rem;
      font-size: 2.4rem;
    }

    .subtitle {
      color: #4b5563;
      margin-bottom: 2rem;
      line-height: 1.5;
    }

    .form-group {
      margin-bottom: 1.5rem;
    }

    label {
      display: block;
      margin-bottom: 0.5rem;
      color: #555;
      font-weight: 500;
    }

    .form-control {
      width: 100%;
      padding: 0.95rem 1rem;
      border: 1px solid rgba(17, 24, 39, 0.12);
      border-radius: 14px;
      font-size: 1rem;
      background: rgba(255, 255, 255, 0.9);
      transition: border-color 0.2s, box-shadow 0.2s, transform 0.2s;
    }

    .form-control:focus {
      outline: none;
      border-color: #c69214;
      box-shadow: 0 0 0 4px rgba(198, 146, 20, 0.16);
      transform: translateY(-1px);
    }

    .error {
      color: #b91c1c;
      font-size: 0.875rem;
      margin-top: 0.25rem;
    }

    .error-message {
      background: rgba(185, 28, 28, 0.08);
      color: #991b1b;
      padding: 0.75rem;
      border-radius: 12px;
      margin-bottom: 1rem;
    }

    .btn-primary {
      width: 100%;
      padding: 0.95rem 1rem;
      background: linear-gradient(135deg, #111827 0%, #273449 100%);
      color: white;
      border: none;
      border-radius: 14px;
      font-size: 1rem;
      font-weight: 600;
      cursor: pointer;
      transition: transform 0.2s, box-shadow 0.2s, opacity 0.2s;
    }

    .btn-primary:hover:not(:disabled) {
      transform: translateY(-1px);
      box-shadow: 0 16px 32px rgba(17, 24, 39, 0.22);
    }

    .btn-primary:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  `]
})
export class LoginComponent {
  loginForm: FormGroup;
  loading = false;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.loading = true;
      this.error = null;

      this.authService.login(this.loginForm.value).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          this.error = err.error?.message || 'Erro ao fazer login';
          this.loading = false;
        }
      });
    }
  }
}
