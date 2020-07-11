using System;
using System.Collections.Generic;
using System.Text;

namespace Linkmon_desktop.model
{
    public class Record
    {
        public long Id { get; set; }
        public long Latency { get; set; }
        public Boolean IsUp { get; set; }
        public DateTime Time { get; set; }
    }
}
