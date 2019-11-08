import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomePage } from './home.page';

const routes: Routes = [
  {
    path: 'home',
    component: HomePage,
    children:[
        
        {
            path: 'notifications',
            loadChildren: () =>
                import('../pages/asignar/notifications.module').then(
                    m => m.NotificationsPageModule
                )
        },
        
        {
            path: 'settings',
            loadChildren: () =>
                import('../pages/settings/settings.module').then(
                    m => m.SettingsPageModule
                )
        }
    ]
}];
@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class HomeRouter {}