using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using EnvDTE;

namespace Pasta
{
    public class Variables
    {
        public static string _token = "";

        public static readonly string Path = Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData) +
                                              "/Pasta.ini";

        public const string TokenSuffix = "{TOKEN}";

        /* URL BASES */
        public const string UrlPaste = "http://www.ioncodes.com/Pasta/create.php?token={TOKEN}";
        public const string UrlCreateToken = "http://www.ioncodes.com/Pasta/createtoken.php";
    }
}
