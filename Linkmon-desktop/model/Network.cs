using System;
using System.Collections.Generic;
using System.Text;

namespace Linkmon_desktop.model
{
    public class Network
    {
        public long Id { get; set; }
        public string Uuid { get; set; }
        public string Name { get; set; }
        public Machine Machine { get; set; }
    }
}
