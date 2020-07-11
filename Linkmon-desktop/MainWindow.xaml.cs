using Linkmon_desktop.model;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace Linkmon_desktop
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        private CancellationTokenSource cancellationToken;

        public MainWindow()
        {
            InitializeComponent();
        }

        private void Window_Loaded(object sender, RoutedEventArgs e)
        {
            var desktopWorkingArea = SystemParameters.WorkArea;
            this.Left = desktopWorkingArea.Right - this.Width;
            this.Top = desktopWorkingArea.Bottom - this.Height;
        }

        private async Task LoadData(string uuid)
        {
            var data = await ApiClient.LoadRecent(uuid);
            NetworkListBox.ItemsSource = data;
        }

        private async void Button_Click(object sender, RoutedEventArgs e)
        {
            if(cancellationToken != null)
            {
                cancellationToken.Cancel();
            }
            else
            {
                cancellationToken = new CancellationTokenSource();
            }
            if(ApiClient.Client == null)
            {
                ApiClient.InitializeClient(Host.Text);
            }
            while (true)
            {
                try
                {
                    await LoadData(UUIDInput.Text);
                    if (cancellationToken.IsCancellationRequested)
                        break;
                    await Task.Delay(5, cancellationToken.Token);
                }
                catch (TaskCanceledException)
                {
                    break;
                }
            }
            
        }

    }
    public class RecordsConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            StringBuilder sb = new StringBuilder();
            foreach (Record r in (Record[])value)
            {
                if (r.IsUp)
                {
                    sb.Append(r.Latency + "ms,");
                }
                else
                {
                    sb.Append("Fail,");
                }
            }
            if (sb.Length > 0)
            {
                sb.Remove(sb.Length - 1, 1);
            }
            return sb.ToString();
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}
