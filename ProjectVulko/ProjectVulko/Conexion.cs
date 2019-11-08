using System;
using System.Collections.Generic;
using System.Data;
using System.Data.OleDb;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MySql.Data.MySqlClient;
namespace ProjectVulko
{
    class Conexion
    {
        private MySqlConnection Conect = new MySqlConnection("Server=flumpto.site;Port=3306;Database=flumptos_Restaurant;Uid=flumptos_Admin;Pwd=1234567890;");
        private DataSet Ds;
        public void Conectar()
        {

            Conect.Open();

        }
        public void Desconectar()
        {
            Conect.Close();
        }
        public void Insertar(string campo1, string campo2, string campo3, string campo4, MemoryStream campo5)

        {
            int Item_ID = Convert.ToInt32(campo1);
            string Nombre = campo2;
            string Descripcion = campo3;
            int Precio = Convert.ToInt32(campo4);
            MemoryStream Imagen = campo5;
            try
            {
                MySqlCommand insertar = new MySqlCommand(string.Format("INSERT INTO Menu VALUES ('" + Item_ID + "','" + Nombre + "','" + Descripcion + "','" + Precio + "','" + Imagen + "')"),Conect);
                insertar.ExecuteNonQuery();
                MessageBox.Show("Inserción Realizada");
            }

            catch (DBConcurrencyException ex)
            {
                MessageBox.Show("Error de concurrencia:\n" + ex.Message);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

    }
}
