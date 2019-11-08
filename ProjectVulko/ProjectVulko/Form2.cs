using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MySql.Data.MySqlClient;

namespace ProjectVulko
{
    public partial class Form2 : Form
    {
        private MySqlConnection Conect = new MySqlConnection("Server=flumpto.site;Port=3306;Database=flumptos_Restaurant;Uid=flumptos_Admin;Pwd=1234567890;");
        MySqlCommand command;
        MySqlDataAdapter da;
        DataTable table = new DataTable();
        public Form2()
        {
            InitializeComponent();
        }

        private void Close_Click(object sender, EventArgs e)
        {
            Application.Exit();
        }

        private void Minimizar_Click(object sender, EventArgs e)
        {
            WindowState = FormWindowState.Minimized;
        }
        public static void LlenarCombox(string Query, ComboBox comb)
        {
            Form2 f2 = new Form2();
            MySqlDataReader reader;
            try
            {

                f2.Conect.Open();

                if (comb.Items.Count > 0)
                    comb.Items.Clear();

                MySqlCommand cmd = new MySqlCommand(Query, f2.Conect);
               reader = cmd.ExecuteReader();

                while (reader.Read())
                    comb.Items.Add(reader.GetString("Name"));

                reader.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message.ToString());
            }
            f2.Conect.Close();
        }

 //SIDEBAR//////////
        private void Menu_SideBar_Click(object sender, EventArgs e)
        {
            Menu_PanelTop.BringToFront();
            Menu_Panel.BringToFront() ;
            Mostar_Items.Visible = false;
            Mostrar_Button.Visible = true;
            Mostar_Pedidos.Visible = false;
            table.Clear();
            table.Columns.Clear();
            EliminarPedido.Visible = false;
            Eliminar_Pedido.Visible = false;


        }

        private void Modificar_SideBar_Click(object sender, EventArgs e)
        {
            Items_Panel.BringToFront();
            Item_PanelTop.BringToFront();
  
            LlenarCombox("Select * FROM menu", Categoria_Combox);
        }

        private void Pedidos_SideBar_Click(object sender, EventArgs e)
        {
            Pedidos_PanelTop.BringToFront();
            Menu_Panel.BringToFront();
            Mostar_Items.Visible = false;
            Mostrar_Button.Visible = false;
            Mostar_Pedidos.Visible = true;
          //  table.Rows.Clear();
            table.Clear();
            table.Columns.Clear();
            EliminarPedido.Visible = true;
            Eliminar_Pedido.Visible = true;

        }
        private void Categorias_Btn_Click(object sender, EventArgs e)
        {
            Categorias_Paneltop.BringToFront();
            Categorias_Panel.BringToFront();
        }
        private void MenuItems_Click(object sender, EventArgs e)
        {
            
            MenuItems_PanelTop.BringToFront();
            Menu_Panel.BringToFront();
            Mostar_Items.Visible = true;
            Mostrar_Button.Visible = false;
            Mostar_Pedidos.Visible = false;
            table.Clear();
            table.Columns.Clear();
            EliminarPedido.Visible = false;
            Eliminar_Pedido.Visible = false;

        }
        // Botton ver Menu
        
        private void MostrarDatos(string selectQuery)
        {

      
            command = new MySqlCommand(selectQuery, Conect);
            da = new MySqlDataAdapter(command);

            

            MenuDataGrid.AutoSizeColumnsMode = DataGridViewAutoSizeColumnsMode.Fill;
            MenuDataGrid.RowTemplate.Height = 100;
            MenuDataGrid.AllowUserToAddRows = false;

            da.Fill(table);

            MenuDataGrid.DataSource = table;
           
           

          /*  DataGridViewImageColumn imageColumn = new DataGridViewImageColumn();
            imageColumn = (DataGridViewImageColumn)MenuDataGrid.Columns[4];
            imageColumn.ImageLayout = DataGridViewImageCellLayout.Stretch;*/

            da.Dispose();
           

        }

        private void Mostrar_Button_Click(object sender, EventArgs e)
        {
            table.Clear();
            MostrarDatos("SELECT * FROM menu");
        }
        private void Mostar_Pedidos_Click(object sender, EventArgs e)
        {
            table.Clear();
            MostrarDatos("SELECT * FROM Orden");
        }
        private void Mostar_Items_Click(object sender, EventArgs e)
        {
            table.Clear();
            MostrarDatos("SELECT * FROM Productos");
        }

        //-------------////////////////////////
        private void EjecutarQuery(MySqlCommand mcomd, string msg)
        {
            Conect.Open();
            if (mcomd.ExecuteNonQuery() == 1)
            {
                MessageBox.Show(msg);
            }
            else
            {
                MessageBox.Show("Acción no realizada");
            }
            Conect.Close();
        }
        ///BOTONES DE CATEGORIAS
        private void Insert_Categ_Btn_Click(object sender, EventArgs e)
        {


            if (Categ_txtbox.Text.Equals(Clave_Primaria_Rept("SELECT * FROM menu WHERE ID = @id")))
            {
                MessageBox.Show("Llave primaria repetida o campo Vacio!!");

            }

         
            else
            {
                if (Categ_txtbox.Text != "")
                {

                    MySqlCommand command = new MySqlCommand("INSERT INTO menu(ID, Name, Link) VALUES (@id,@name,@link)", Conect);
                    command.Parameters.Add("@id", MySqlDbType.Int32).Value = Int32.Parse(Categ_txtbox.Text);
                    command.Parameters.Add("@name", MySqlDbType.Text).Value = nombrecateg_textbox.Text;
                    command.Parameters.Add("@link", MySqlDbType.Text).Value = Link_txtbox.Text;

                    EjecutarQuery(command, "Inserción Realizada");

                }

                else
                {

                    MessageBox.Show("Campo Vacio");
                }
            }


           
        }
        private void Eliminar_Categ_Btn_Click(object sender, EventArgs e)
        {
            MySqlCommand command = new MySqlCommand("DELETE FROM menu WHERE ID=@id", Conect);


            command.Parameters.Add("@id", MySqlDbType.Text).Value = (Categ_txtbox.Text);
  


            EjecutarQuery(command, "Categoría Eliminada");
        }
        private void Actualizar_Categ_Btn_Click(object sender, EventArgs e)
        {
            MySqlCommand command = new MySqlCommand("UPDATE menu SET Name = @name, Link= @link WHRE ID = @id" , Conect);


            command.Parameters.Add("@id", MySqlDbType.Int32).Value = Int32.Parse(Categ_txtbox.Text);
            command.Parameters.Add("@name", MySqlDbType.Text).Value = nombrecateg_textbox.Text;
            command.Parameters.Add("@link", MySqlDbType.Text).Value = Link_txtbox.Text;


            EjecutarQuery(command, "Categoria Actualizada");

        }

        //BOTONES DE PRODUCTOS////////////
        private void Insertar_Button_Click(object sender, EventArgs e)
        {
            if (Id_textbox.Text.Equals(Clave_Primaria_Rept("SELECT * FROM Productos WHERE ID = @id")))
            {
                MessageBox.Show("Llave primaria repetida o campo Vacio!!");

            }


            else
            {
               
                if ((Id_textbox.Text != ""))
                {

                    MySqlCommand command = new MySqlCommand("INSERT INTO Productos(ID, Name, Link,Price,MenuId) VALUES (@id,@name,@link,@price,@menu_id)", Conect);
                    command.Parameters.Add("@id", MySqlDbType.Int32).Value = Int32.Parse(Id_textbox.Text);
                    command.Parameters.Add("@name", MySqlDbType.Text).Value = Nombre_textbox.Text;
                    command.Parameters.Add("@link", MySqlDbType.Text).Value = Link_textbox_items.Text;
                    command.Parameters.Add("@price", MySqlDbType.Text).Value = Precio_textbox.Text;
                    command.Parameters.Add("@menu_id", MySqlDbType.Text).Value = get_data_from_query();

                    EjecutarQuery(command, "Inserción Realizada");
                }
                else
                {
                    MessageBox.Show("Campo Vacio");
                }

            }
        }

        private void Eliminar_Item_Btn_Click(object sender, EventArgs e)
        {
            MySqlCommand command = new MySqlCommand("DELETE FROM Productos WHERE ID=@id", Conect);



            command.Parameters.Add("@id", MySqlDbType.Int32).Value = Int32.Parse(Id_textbox.Text);
       

            EjecutarQuery(command, "Item eliminado");
        }

        private void Actualizar_Item_Btn_Click(object sender, EventArgs e)
        {
            MySqlCommand command = new MySqlCommand("UPDATE Productos SET Name = @name, Link = @link , Price = @price, MenuId = @menu_id WHERE ID=@id", Conect);

            string seleccionado = this.Categoria_Combox.GetItemText(this.Categoria_Combox.SelectedItem);
            MySqlCommand combobox = new MySqlCommand("SELECT MenuId FROM Productos WHERE Name= @seleccionado", Conect);
            command.Parameters.Add("@seleccionado", MySqlDbType.Text).Value = seleccionado;



            command.Parameters.Add("@id", MySqlDbType.Int32).Value = Int32.Parse(Id_textbox.Text);
            command.Parameters.Add("@name", MySqlDbType.Text).Value = Nombre_textbox.Text;
            command.Parameters.Add("@link", MySqlDbType.Text).Value = Link_textbox_items.Text;
            command.Parameters.Add("@price", MySqlDbType.Text).Value = Precio_textbox.Text;
            command.Parameters.Add("@menu_id", MySqlDbType.Text).Value = get_data_from_query() ;

            EjecutarQuery(command, "Actualización Realizada");
        }

    
        public string get_data_from_query()
        {
            Form2 f3 = new Form2();
            MySqlDataReader reader;
            string data = "";
            try
            {
                f3.Conect.Open();
                string seleccionado = (Categoria_Combox.SelectedItem).ToString();
                MySqlCommand datos = new MySqlCommand("SELECT ID FROM menu WHERE Name = @seleccionado", f3.Conect);
                datos.Parameters.Add("@seleccionado", MySqlDbType.Text).Value = seleccionado;
                reader = datos.ExecuteReader();
                while (reader.Read())
                {
                    data = reader.GetString("ID");

                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            f3.Conect.Close();
            return data;
        }
        public string Clave_Primaria_Rept(String Query)
        {
            Form2 f3 = new Form2();
            MySqlDataReader reader;
            string data = "";
            try
            {
                f3.Conect.Open();
                string id = Categ_txtbox.Text.ToString();
                MySqlCommand datos = new MySqlCommand(Query, f3.Conect);
          
                datos.Parameters.Add("@id", MySqlDbType.Text).Value = id;
                reader = datos.ExecuteReader();
                while (reader.Read())
                {
                    data = reader.GetString("ID");

                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }

            f3.Conect.Close();
            return data;
        }
        ////Solo numeros
        private void textBox1_KeyPress(object sender, KeyPressEventArgs e)
        {
            if(!char.IsControl(e.KeyChar) && !char.IsDigit(e.KeyChar))
           {
                e.Handled = true;

            }
      
        }

        private void EliminarPedido_Click(object sender, EventArgs e)
        {
            MySqlCommand command = new MySqlCommand("DELETE FROM Orden WHERE OrderId= @orderid", Conect);

            if (Eliminar_Pedido.Text != "")
            {
                command.Parameters.Add("@orderid", MySqlDbType.Int64).Value = Int64.Parse(Eliminar_Pedido.Text);
                EjecutarQuery(command, "Pedido eliminado");
            }
            else
            {
                MessageBox.Show("Campo Vacio");
            }

            
        }
    }

}
