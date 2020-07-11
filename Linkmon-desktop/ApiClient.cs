using Linkmon_desktop.model.response;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;

namespace Linkmon_desktop
{
    public class ApiClient
    {
        public static HttpClient Client { get; set; }

        public static void InitializeClient(string host)
        {
            Client = new HttpClient();
            Client.BaseAddress = new Uri(host);
            Client.DefaultRequestHeaders.Accept.Clear();
            Client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
        }

        public async static Task<RecentResponse[]> LoadRecent(string uuid)
        {
            if (Client == null)
            {
                InitializeClient("http://localhost:8080/");
            }
            string url = $"api/machine/{uuid}/recent";
            using (HttpResponseMessage response = await Client.GetAsync(url))
            {
                if(response.IsSuccessStatusCode)
                {
                    RecentResponse[] recent = await response.Content.ReadAsAsync<RecentResponse[]>();
                    return recent;
                }
                else
                {
                    throw new Exception(response.ReasonPhrase);
                }
            }
        }
    }
}
