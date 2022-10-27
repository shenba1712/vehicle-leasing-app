import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import {MatIconModule} from "@angular/material/icon";
import {RouterTestingModule} from "@angular/router/testing";
import {MatButtonModule} from "@angular/material/button";
import {By} from "@angular/platform-browser";

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        MatButtonModule,
        MatIconModule
      ],
      declarations: [ DashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
    expect(fixture.debugElement.query(By.css('h1')).nativeElement.textContent).toEqual('Welcome! What would you like to do today?');
    expect(fixture.debugElement.queryAll(By.css('button')).length).toEqual(3);
    expect(fixture.debugElement.queryAll(By.css('button'))[0].nativeElement.textContent).toContain('Customer Management');
    expect(fixture.debugElement.queryAll(By.css('button'))[1].nativeElement.textContent).toContain('Vehicle Management');
    expect(fixture.debugElement.queryAll(By.css('button'))[2].nativeElement.textContent).toContain('Contract Management');
  });
});
