import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
//import { FirebaseAuthentication } from '@ionic-native/firebase-authentication/ngx';
import { AuthService } from '../../services/auth.service';
import { User } from '../../shared/user.class';
@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
})
export class SignupPage implements OnInit {

  user: User = new User();
  constructor(private router:Router, private authSvc: AuthService) { }

  ngOnInit() {
  }

  async signup(){
    const user = await this.authSvc.onRegister(this.user);

    //this.authSvc.afAuth.auth.createUserWithEmailAndPassword("my@mail.com", "pa55w0rd");
    if (user) {
      this.router.navigate(["/home/notifications"]);
    }
    
  }

}
