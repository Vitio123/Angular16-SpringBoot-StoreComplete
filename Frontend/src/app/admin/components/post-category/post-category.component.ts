import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from '../../service/admin.service';
import { Router } from '@angular/router';
import { UserStorageService } from 'src/app/services/storage/user-storage.service';

@Component({
  selector: 'app-post-category',
  templateUrl: './post-category.component.html',
  styleUrls: ['./post-category.component.css'],
})
export class PostCategoryComponent {
  categoryForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.categoryForm = this.fb.group({
      name: [null, [Validators.required]],
      description: [null, [Validators.required]],
    });
  }

  addCategory(): void {
    console.log('Form valid:', this.categoryForm.valid);
    console.log('Form value:', this.categoryForm.value);
    console.log('Token exists:', !!UserStorageService.getToken());

    if (this.categoryForm.valid) {
      this.adminService.addCategory(this.categoryForm.value).subscribe({
        next: (res) => {
          console.log('Success response:', res);
          if (res.id != null) {
            this.snackBar.open('Category Posted Successfully!', 'Close', {
              duration: 5000,
            });
            this.router.navigateByUrl('/admin/dashboard');
          } else {
            this.snackBar.open(res.message, 'Close', {
              duration: 5000,
              panelClass: 'error-snackbar',
            });
          }
        },
        error: (error) => {
          console.error('Component error:', error);
          this.snackBar.open(
            'Error: ' +
              (error.error?.message || error.message || 'Unknown error'),
            'Close',
            {
              duration: 5000,
              panelClass: 'error-snackbar',
            }
          );
        },
      });
    } else {
      this.categoryForm.markAllAsTouched();
    }
  }
}
