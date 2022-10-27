import {ComponentFixture, TestBed} from '@angular/core/testing';
import { AppComponent } from './app.component';
import {FontAwesomeModule} from "@fortawesome/angular-fontawesome";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatMenuModule} from "@angular/material/menu";
import {MatIconModule} from "@angular/material/icon";
import {RouterTestingModule} from "@angular/router/testing";
import {By} from "@angular/platform-browser";

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let app: AppComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        FontAwesomeModule,
        MatToolbarModule,
        MatButtonModule,
        MatMenuModule,
        MatIconModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
  });

  it(`should have as title 'Contract Leasing App'`, () => {
    expect(app).toBeTruthy();
    expect(app.title).toEqual('Contract Leasing App');
  });

  it('should have the toolbar with name', () => {
    expect(fixture.debugElement.query(By.css('mat-toolbar')).nativeElement).toBeTruthy();
    expect(fixture.debugElement.query(By.css('mat-toolbar a')).nativeElement.textContent)
      .toEqual(' Allane Car Leasing ');
  });
});
