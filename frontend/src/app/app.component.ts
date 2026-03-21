import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <div class="app-container">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .app-container {
      min-height: 100vh;
      background:
        radial-gradient(circle at top, rgba(214, 160, 34, 0.12), transparent 32%),
        linear-gradient(180deg, #f6f1e7 0%, #f3f4f7 100%);
    }
  `]
})
export class AppComponent {
  title = 'SAP ERP Enterprise';
}
