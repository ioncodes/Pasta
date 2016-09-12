using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using Windows.Data.Xml.Dom;
using Windows.UI.Notifications;
using System.ComponentModel.Design;
using System.Runtime.CompilerServices;
using Microsoft.VisualStudio.Shell;
using EnvDTE;

namespace Pasta
{
    public class Functions
    {
        public enum Caller
        {
            Paste,
            Browser,
            PasteBrowser    
        }

        public static string GetDocumentText(Document document)
        {
            var textDocument = (TextDocument)document.Object("TextDocument");
            EditPoint editPoint = textDocument.StartPoint.CreateEditPoint();
            var content = editPoint.GetText(textDocument.EndPoint);
            return content;
        }


        public static string WebPost(string url, string postData)
        {
            HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";

            byte[] data = Encoding.ASCII.GetBytes(postData);
            request.ContentLength = data.Length;
            using (var stream = request.GetRequestStream())
            {
                stream.Write(data, 0, data.Length);
            }
            var response = (HttpWebResponse)request.GetResponse();
            var responseString = new StreamReader(response.GetResponseStream()).ReadToEnd();

            switch (responseString)
            {
                case "Wrong request.":
                    MessageBox.Show("Internal error. Please report this to the developer.");
                    return "";
                case "DB Error.":
                    MessageBox.Show("Server not working currently.");
                    return "";
                case "Error creating file.":
                    MessageBox.Show("Could not paste. Make sure a valid file is open.");
                    return "";
            }

            return responseString;
        }

        public static void SendText(DTE dte, Guid cmdSet, Caller call)
        {
            if (Variables._token == "") return;
            // METHOD: CREATE PASTE
            if (dte.ActiveDocument != null)
            {
                string source = Functions.GetDocumentText(dte.ActiveDocument);
                if (source != "")
                {
                    string postData = "source=" + Uri.EscapeDataString(source);
                    string url = Functions.WebPost(Variables.UrlPaste.Replace(Variables.TokenSuffix, Variables._token),
                        postData);
                    Clipboard.SetText(url);
                    switch (call)
                    {
                        case Caller.Paste:
                            Notify("Your paste has been published! Copied link into clipboard!", cmdSet); // Toast
                            break;
                        case Caller.Browser:
                            Notify("Your paste has been published! Opening browser!", cmdSet); // Toast
                            break;
                        case Caller.PasteBrowser:
                            Notify("Your paste has been published! Copied link into clipboard and opening browser!", cmdSet); // Toast
                            break;
                    }
                }
                else
                {
                    MessageBox.Show("Source cannot be empty!");
                }
            }
            else
            {
                MessageBox.Show("No valid file opened!");
            }
        }

        // HELPER METHOD! Refactor into separate helper class!
        public static void Notify(string message, Guid cmdSet)
        {
            // Get a toast XML template
            XmlDocument toastXml = ToastNotificationManager.GetTemplateContent(ToastTemplateType.ToastText02);

            // Fill in the text elements
            XmlNodeList stringElements = toastXml.GetElementsByTagName("text");
            stringElements[0].AppendChild(toastXml.CreateTextNode("Pasta Notification"));
            stringElements[1].AppendChild(toastXml.CreateTextNode(message));
            //stringElements[2].AppendChild(toastXml.CreateTextNode("Any text")); // Possible third line

            ToastNotification toast = new ToastNotification(toastXml);
            toast.Activated += ToastActivated;

            ToastNotificationManager.CreateToastNotifier(cmdSet.ToString()).Show(toast);
            // CommandSet could practically be any kind of hardcoded ID
        }

        private static void ToastActivated(ToastNotification sender, object args)
        {
            // Do we want to do anything here?
        }
    }
}
