using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MySql.Data.MySqlClient;

namespace ProjectVulko
{
    public partial class Form1 : Form
    {
        private MySqlConnection Conect = new MySqlConnection("Server=flumpto.site;Port=3306;Database=flumptos_Restaurant;Uid=flumptos_Admin;Pwd=1234567890;");
        public Form1()
        {
            InitializeComponent();
        }
        private string Usuario()
        {
            MySqlDataReader reader;
            string user = "";
            try
            {
                Conect.Open();
                MySqlCommand command = new MySqlCommand("SELECT * from usuariosAdmin WHERE user=@user", Conect);
                command.Parameters.Add("@user", MySqlDbType.Text).Value = Usertxtbox.Text.ToString();
                reader = command.ExecuteReader();
                while (reader.Read())
                {
                    user = reader.GetString("user");

                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            Conect.Close();
            return user;
        }
        
        private String Contraseña()
        {
            MySqlDataReader reader;
            string pwd = "";
            try
            {
                Conect.Open();
                MySqlCommand command = new MySqlCommand("SELECT * from usuariosAdmin WHERE user=@user", Conect);
                command.Parameters.Add("@user", MySqlDbType.Text).Value = Usertxtbox.Text.ToString();
                reader = command.ExecuteReader();
                while (reader.Read())
                {
                    pwd = reader.GetString("pwd");

                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            Conect.Close();
            return pwd;

        }

        private void Minimize_Click(object sender, EventArgs e)
        {
            WindowState = FormWindowState.Minimized;
        }

        private void Cloos_Click(object sender, EventArgs e)
        {
            Application.Exit() ;
        }

        private void BunifuThinButton21_Click(object sender, EventArgs e)
        {
            String Usuario1 = Usertxtbox.Text.ToString();
            String Contraseña1 = Pwdtxtbox.Text.ToString();

            if (Usuario1 == Usuario() && Contraseña1 == Contraseña())
            {
                Form2 Menu = new Form2();
                Menu.ShowDialog();
                this.Close();
            }
            else
            {
                MessageBox.Show("Usuario o Contraseña Incorrectos");
            }

        }

    
    }
}
