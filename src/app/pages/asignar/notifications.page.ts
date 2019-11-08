import { Component, OnInit } from '@angular/core';
import { Geolocation } from '@ionic-native/geolocation/ngx';

import { LoadingController } from '@ionic/angular';
declare var google;

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.page.html',
  styleUrls: ['./notifications.page.scss'],
})
export class NotificationsPage implements OnInit {
  mapRef = null;

  constructor(
    private geolocation: Geolocation,
    private loadingCtrl: LoadingController
  ) { }

  ngOnInit() {
    this.loadMap();

    
    
  }



  async loadMap(){

    const loading = await this.loadingCtrl.create();
    loading.present();
    const myLating = await this.getLocation();

    //console.log(myLating); imprime en la consola
    const mapEle: HTMLElement = document.getElementById('map');
    this.mapRef  = new google.maps.Map(mapEle, {
      center: myLating,
      zoom: 20
    });

    google.maps.event.addListener(this.mapRef, "click", function (e) {
      
      
      
       
     
     
      const marker = new google.maps.Marker({
        position: {
          lat1 : e.latLng.lat,
          lat2 : e.latLng.lng
        },
        zoom: 8,
        map: this.mapRef,
        title: 'Hello World!'
      });

      console.log("Agregado la puta madre sossi en: ",e.latLng.lat(),e.latLng.lng());
    });

    google.maps.event
    .addListenerOnce(this.mapRef, 'idle', () => {
     loading.dismiss();
     this.addMarker(myLating.lat,myLating.lng);

    });

    
    
  }

  

  public addMarker(lat: number, lng: number){
    const marker = new google.maps.Marker({
      position: { 
        lat,
        lng
      },
      zoom: 8,
      map: this.mapRef,
      title: 'Hello World!'
    });
    console.log("Agregado en: ",lat,lng);
  }

  
  private async getLocation(){
    const rta = await this.geolocation.getCurrentPosition();
    return {
      lat: rta.coords.latitude,
      lng: rta.coords.longitude

      //obtiene posicion del usuario
    };

  }
     
}
