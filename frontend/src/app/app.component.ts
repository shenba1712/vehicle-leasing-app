import { Component } from '@angular/core';
import { faCaravan } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'Contract Leasing App';
  caravan = faCaravan;
}
