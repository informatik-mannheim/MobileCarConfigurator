package sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.communication;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import sysplace.st.tud.mobilecarconfigurator.sysplace.st.tud.mobilecarconfigurator.data.CarColor;

/**
 * Created by cpiechnick on 30/05/16.
 */
public class ServerCommunicator {

    String swipeKey = "cas_mmobile_swipe_data";

    public void store(CarColor color) {
        String text = "{\n" +
                "\t\"product\": {\n" +
                "\t\t\"attributeGroups\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\": \"Exterior\",\n" +
                "\t\t\t\t\"attributes\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"Farbe\",\n" +
                "\t\t\t\t\t\t\"selectedValues\": [\n" +
                "\t\t\t\t\t\t\t\"" + (color == CarColor.Green ? "Grün" : "Blau") + "\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"Scheibentönung\",\n" +
                "\t\t\t\t\t\t\"selectedValues\": [\n" +
                "\t\t\t\t\t\t\t\"Ja\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"Felgen\",\n" +
                "\t\t\t\t\t\t\"selectedValues\": [\n" +
                "\t\t\t\t\t\t\t\"Felge B\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"name\": \"Interior\",\n" +
                "\t\t\t\t\"attributes\": [\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"Polster\",\n" +
                "\t\t\t\t\t\t\"selectedValues\": [\n" +
                "\t\t\t\t\t\t\t\"Leder\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\t{\n" +
                "\t\t\t\t\t\t\"name\": \"Navigation\",\n" +
                "\t\t\t\t\t\t\"selectedValues\": [\n" +
                "\t\t\t\t\t\t\t\"Ja\"\n" +
                "\t\t\t\t\t\t]\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";
        new HttpRequestTask("config-cas", text).execute();
    }

    public String getNewColor() {
        try {
            String result = new HttpGetRequestTask().execute().get();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "n";
        }

    }

    public void initiateExchange(CarColor color) {
        new HttpRequestTask("new-color", color == CarColor.Green ? "g" : "b").execute();
    }

    public void clearExchange() {
        new HttpRequestTask("new-color", "n").execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, String> {
        private String value;
        private String key;

        public HttpRequestTask(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection client = null;

            try {
                String newVal = URLEncoder.encode(value, "UTF-8");
                URL url = new URL("http://172.31.1.48:8080/string-store/set?key=" + key + "&value=" + newVal);
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");

                OutputStream outputPost = new BufferedOutputStream(client.getOutputStream());
                outputPost.flush();
                outputPost.close();

                int responseCode = client.getResponseCode();
                int i = 0;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                client.disconnect();

            }

            return null;
        }

        @Override
        protected void onPostExecute(String greeting) {
            //TextView greetingIdText = (TextView) findViewById(R.id.id_value);
            //TextView greetingContentText = (TextView) findViewById(R.id.content_value);
            //greetingIdText.setText(greeting.getId());
            //greetingContentText.setText(greeting.getContent());
        }

    }

    private class HttpGetRequestTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection client = null;
            String result = "";

            try {
                URL url = new URL("http://172.31.1.48:8080/string-store/get?key=new-color");
                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        client.getInputStream()));
                String inputLine = "";

                while ((inputLine = in.readLine()) != null) {
                    result += inputLine;
                }

                in.close();
            } catch (Exception e) {
                result = "n";
                e.printStackTrace();
            } finally {
                client.disconnect();

            }

            return result;
        }
    }


}
