

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import javax.xml.transform.Transformer;
import java.io.*;
import java.net.*;


public class themovieDBproject {

    private int indiceActor = 0;
    protected String strIp, databaseName, usuario, contrasenya;

    //constructor
    public themovieDBproject(String strIp, String databaseName, String usuario, String contrasenya){
        this.strIp = strIp;
        this. databaseName = databaseName;
        this.usuario = usuario;
        this.contrasenya = contrasenya;
        connexioApi();
    }

   /**
    * Conexion a la web "https://api.themoviedb.org"
    * para el tratamiento de datos de peliculas en formato JSON
    */
    public void connexioApi(){
        String JsonPelis = "";
        String JsonActors = "";
        String api_key = "3a612a7f772ad50863c9994c3c7b9204";

        //System.out.println("PELICULAS:\n");

        for (int i = 0; i < 10; i++) {
            int peliId = 620 + i;
            String film = String.valueOf(peliId);
            String peticioActors = "https://api.themoviedb.org/3/movie/"+ film +"/credits?api_key="+api_key;
            String peticioPeli = "https://api.themoviedb.org/3/movie/"+ film +"?api_key="+api_key;

            try {
               if(i == 9){
                  JsonPelis = getHTML("https://api.themoviedb.org/3/movie/137?api_key="+api_key);//  peticioPeli
                  SJS(JsonPelis, i);

                  JsonActors = getHTML("https://api.themoviedb.org/3/movie/137/credits?api_key="+api_key);
                  SJCpersonaje(JsonActors, i);
               }
               else{
                  JsonPelis = getHTML(peticioPeli);//  peticioPeli
                  //System.out.println(JsonPelis);
                  SJS(JsonPelis, i);

                  JsonActors = getHTML(peticioActors);//  peticioActors
                  //System.out.println("\tActors:");
                  //System.out.println("\t" + JsonActors);
                  SJCpersonaje(JsonActors, i);
               }



            } catch (Exception e) {
                System.out.println("La peli "+film+" no existeix");
            }
        }
    }


    /**
     * Retorna el JSON de la pelicula
     * @param urlToRead
     * @return
     * @throws Exception
     */
    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        return result.toString();//retorna un JSON
    }

    /**
     * Escriu la pelicula i la data de publicacio
     * Fa INSERTS de pelicules a la bbdd
     * @param cadena
     */
    public void SJS (String cadena, int i){//para pelis

       Object obj02 =JSONValue.parse(cadena);
       JSONObject arra02=(JSONObject)obj02;

       //System.out.println("\nTitol: " + arra02.get("original_title"));
       String titulo = (String) arra02.get("original_title");

       //System.out.println("Data de publicació: " + arra02.get("release_date") + "\n");
       String fecha = (String) arra02.get("release_date");

       insertSQLite.insertDataPeliculas(i, titulo, fecha, strIp,
               databaseName, usuario, contrasenya);
       System.out.println("...pelicula actualizada");

    }

    /**
     * Escriu tots els personatges
     * Fa INSERTS d actors a la bbdd
     * @param cadena
     */
    public void SJCpersonaje (String cadena, int j){//para personajes
        Object obj02 =JSONValue.parse(cadena);
        JSONObject arra02=(JSONObject)obj02;
        JSONArray arra03 = (JSONArray)arra02.get("cast");

        for (int i = 0; i < arra03.size(); i++) {
           JSONObject jb= (JSONObject)arra03.get(i);
           //System.out.println("\t" + (i+1) + " - " + jb.get("character")+"<-->"+jb.get("name"));

           String nombre_personaje = (String) jb.get("character");
           String nombre_actor = (String) jb.get("name");
           long id_actor = (long) jb.get("id");

           insertSQLite.insertDataActores(indiceActor, nombre_actor, id_actor, nombre_personaje, j,
                   strIp, databaseName, usuario, contrasenya);
           //System.out.println("...actor actualizado");

           indiceActor++;
        }
    }

}


/*
{
   "id":620,
   "cast":[
      {
         "cast_id":18,
         "character":"Dr. Peter Venkman",
         "credit_id":"52fe425fc3a36847f801928b",
         "id":1532,
         "name":"Bill Murray",
         "order":0,
         "profile_path":"/lBXifSLzs1DuspaWkACjSfjlwbd.jpg"
      },
      {
         "cast_id":19,
         "character":"Dr. Raymond Stantz",
         "credit_id":"52fe425fc3a36847f801928f",
         "id":707,
         "name":"Dan Aykroyd",
         "order":1,
         "profile_path":"/h2PT9yZYv5ml5hL9jvCpWBTWgU.jpg"
      },
      {
         "cast_id":26,
         "character":"Dana Barrett",
         "credit_id":"52fe425fc3a36847f80192ab",
         "id":10205,
         "name":"Sigourney Weaver",
         "order":2,
         "profile_path":"/bcDb0vbfWZBHo1QEh9oQVRs3vx2.jpg"
      },
      {
         "cast_id":20,
         "character":"Dr. Egon Spengler",
         "credit_id":"52fe425fc3a36847f8019293",
         "id":1524,
         "name":"Harold Ramis",
         "order":3,
         "profile_path":"/zLzR2UwzyrR9A6RMarZJV833X9K.jpg"
      },
      {
         "cast_id":21,
         "character":"Louis Tully",
         "credit_id":"52fe425fc3a36847f8019297",
         "id":8872,
         "name":"Rick Moranis",
         "order":4,
         "profile_path":"/27dRb7OyRGzQP8D4tzyY6dEdmQY.jpg"
      },
      {
         "cast_id":22,
         "character":"Janine Melnitz",
         "credit_id":"52fe425fc3a36847f801929b",
         "id":8873,
         "name":"Annie Potts",
         "order":5,
         "profile_path":"/gXvjjNtO26Qlwzrkgpv8cDN0IFu.jpg"
      },
      {
         "cast_id":23,
         "character":"Walter Peck",
         "credit_id":"52fe425fc3a36847f801929f",
         "id":7676,
         "name":"William Atherton",
         "order":6,
         "profile_path":"/dtzWBSXsuZB2NeAfTiytTBOnUoB.jpg"
      },
      {
         "cast_id":24,
         "character":"Winston Zeddmore",
         "credit_id":"52fe425fc3a36847f80192a3",
         "id":8874,
         "name":"Ernie Hudson",
         "order":7,
         "profile_path":"/wrMGBA4eNTUj2ympGsxaqcRpHBS.jpg"
      },
      {
         "cast_id":25,
         "character":"Mayor",
         "credit_id":"52fe425fc3a36847f80192a7",
         "id":8875,
         "name":"David Margulies",
         "order":8,
         "profile_path":"/gxzWJuIokIQxVfncRrPOnBbczuq.jpg"
      },
      {
         "cast_id":29,
         "character":"Female Student",
         "credit_id":"52fe425fc3a36847f80192bb",
         "id":101652,
         "name":"Jennifer Runyon",
         "order":9,
         "profile_path":"/uGLNyr16rdaKkskmlXcYwXAw8F.jpg"
      },
      {
         "cast_id":30,
         "character":"Hotel Manager",
         "credit_id":"52fe425fc3a36847f80192bf",
         "id":1080265,
         "name":"Michael Ensign",
         "order":10,
         "profile_path":"/nyTn32fU0E1pQpIUeuiTAehaAee.jpg"
      },
      {
         "cast_id":35,
         "character":"Gozer",
         "credit_id":"52fe425fc3a36847f80192db",
         "id":562314,
         "name":"Slavitza Jovan",
         "order":11,
         "profile_path":"/vZoaWlkaKdyIuvtz5m9gcUwRys.jpg"
      },
      {
         "cast_id":36,
         "character":"Male Student",
         "credit_id":"52fe425fc3a36847f80192df",
         "id":55930,
         "name":"Steven Tash",
         "order":12,
         "profile_path":"/loYE3mTbF10DvIDBdjUAqebwGaT.jpg"
      },
      {
         "cast_id":37,
         "character":"Librarian",
         "credit_id":"52fe425fc3a36847f80192e3",
         "id":17488,
         "name":"Alice Drummond",
         "order":13,
         "profile_path":"/f1O8suDhVloKdPvhyltj017SPTo.jpg"
      },
      {
         "cast_id":38,
         "character":"Dean Yager",
         "credit_id":"52fe425fc3a36847f80192e7",
         "id":51549,
         "name":"Jordan Charney",
         "order":14,
         "profile_path":"/iG7EkLD4xAmmoUmRwQZhOcWrtgU.jpg"
      },
      {
         "cast_id":39,
         "character":"Violinist",
         "credit_id":"52fe425fc3a36847f80192eb",
         "id":17396,
         "name":"Timothy Carhart",
         "order":15,
         "profile_path":"/1UEUs6UJdxjSbGakZflWuN4TS7T.jpg"
      },
      {
         "cast_id":40,
         "character":"Library Administrator",
         "credit_id":"52fe425fc3a36847f80192ef",
         "id":60205,
         "name":"John Rothman",
         "order":16,
         "profile_path":"/bRSoDm75c5fKBbCrbrXwaPiAeST.jpg"
      },
      {
         "cast_id":41,
         "character":"Archbishop",
         "credit_id":"52fe425fc3a36847f80192f3",
         "id":1164427,
         "name":"Tom McDermott",
         "order":17,
         "profile_path":"/eYjfVZikn6XGhyl9D8VXDCcezrr.jpg"
      },
      {
         "cast_id":42,
         "character":"Fire Commissioner",
         "credit_id":"52fe425fc3a36847f80192f7",
         "id":80148,
         "name":"John Ring",
         "order":18,
         "profile_path":"/hW3E41OhhwAnp1RaAi7EZhIWpTk.jpg"
      },
      {
         "cast_id":43,
         "character":"Police Commissioner",
         "credit_id":"52fe425fc3a36847f80192fb",
         "id":84684,
         "name":"Norman Matlock",
         "order":19,
         "profile_path":"/3XI2Zu0OwPgwYNw7j1NSwecCPEt.jpg"
      },
      {
         "cast_id":44,
         "character":"Police Captain",
         "credit_id":"52fe425fc3a36847f80192ff",
         "id":189816,
         "name":"Joe Cirillo",
         "order":20,
         "profile_path":"/9jT78pW35ZQkqQiGU9lcUbmPGa3.jpg"
      },
      {
         "cast_id":45,
         "character":"Police Seargeant",
         "credit_id":"52fe425fc3a36847f8019303",
         "id":1164428,
         "name":"Joe Schmieg",
         "order":21,
         "profile_path":"/9FfYwOqXRtTxv9LUCPZjLaTu59l.jpg"
      },
      {
         "cast_id":46,
         "character":"Himself",
         "credit_id":"52fe425fc3a36847f8019307",
         "id":950927,
         "name":"Roger Grimsby",
         "order":22,
         "profile_path":"/8KMgeVezUJVlkOMvBGiXQJQ8mYL.jpg"
      },
      {
         "cast_id":47,
         "character":"Himself",
         "credit_id":"52fe425fc3a36847f801930b",
         "id":44127,
         "name":"Larry King",
         "order":23,
         "profile_path":"/bx1m0FpxGUBposPVY960fOtto8c.jpg"
      },
      {
         "cast_id":48,
         "character":"Himself",
         "credit_id":"52fe425fc3a36847f801930f",
         "id":106223,
         "name":"Joe Franklin",
         "order":24,
         "profile_path":"/p4FMZfdBRdDTfgLI8OUUQLSseWo.jpg"
      },
      {
         "cast_id":49,
         "character":"Himself",
         "credit_id":"52fe425fc3a36847f8019313",
         "id":16418,
         "name":"Casey Kasem",
         "order":25,
         "profile_path":"/ioQiaAGO3s6oNaxiIuT21a2RWKj.jpg"
      },
      {
         "cast_id":50,
         "character":"Jail Guard",
         "credit_id":"52fe425fc3a36847f8019317",
         "id":7672,
         "name":"Reginald VelJohnson",
         "order":26,
         "profile_path":"/dZtIIOI0PpD3dmTVfUOWiQzOY28.jpg"
      },
      {
         "cast_id":51,
         "character":"Real Estate Woman",
         "credit_id":"52fe425fc3a36847f801931b",
         "id":157996,
         "name":"Rhoda Gemignani",
         "order":27,
         "profile_path":"/5naUTp9Bdc44cDRAPLJ4nnTYKCv.jpg"
      },
      {
         "cast_id":52,
         "character":"Man at Elevator",
         "credit_id":"52fe425fc3a36847f801931f",
         "id":158287,
         "name":"Murray Rubin",
         "order":28,
         "profile_path":"/vdyZfe9gvBF6GP7JXk82GXt9NGM.jpg"
      },
      {
         "cast_id":72,
         "character":"Con Edison Man",
         "credit_id":"52fe425fc3a36847f8019367",
         "id":1164429,
         "name":"Larry Dilg",
         "order":29,
         "profile_path":"/ucJKtoQ6nZ4I6Hze8i1W2N370J5.jpg"
      },
      {
         "cast_id":53,
         "character":"Coachman",
         "credit_id":"52fe425fc3a36847f8019323",
         "id":184770,
         "name":"Danny Stone",
         "order":30,
         "profile_path":"/6ue5EEQvhmhwhFlqwWRZ5vTDy19.jpg"
      },
      {
         "cast_id":54,
         "character":"Woman at Party",
         "credit_id":"52fe425fc3a36847f8019327",
         "id":157512,
         "name":"Patty Dworkin",
         "order":31,
         "profile_path":"/rmCCZNawobtB8r4HOCvvb2vUfv8.jpg"
      },
      {
         "cast_id":55,
         "character":"Tall Woman at Party",
         "credit_id":"52fe425fc3a36847f801932b",
         "id":158765,
         "name":"Jean Kasem",
         "order":32,
         "profile_path":"/2NGmXjrm356mzFH86JdNJ6cxlzo.jpg"
      },
      {
         "cast_id":56,
         "character":"Doorman",
         "credit_id":"52fe425fc3a36847f801932f",
         "id":1164430,
         "name":"Lenny Del Genio",
         "order":33,
         "profile_path":"/3T5TF5cHiRtO1wDKeysr197b4vc.jpg"
      },
      {
         "cast_id":57,
         "character":"Chambermaid",
         "credit_id":"52fe425fc3a36847f8019333",
         "id":172538,
         "name":"Frances E. Nealy",
         "order":34,
         "profile_path":"/h8BXU3Doqum2mCd551qFvw5KRXj.jpg"
      },
      {
         "cast_id":58,
         "character":"Hot Dog Vendor",
         "credit_id":"52fe425fc3a36847f8019337",
         "id":185147,
         "name":"Sam Moses",
         "order":35,
         "profile_path":"/sGYtyX8M4gvYJxWdiYYBvB9Dmjf.jpg"
      },
      {
         "cast_id":59,
         "character":"TV Reporter",
         "credit_id":"52fe425fc3a36847f801933b",
         "id":65924,
         "name":"Christopher Wynkoop",
         "order":36,
         "profile_path":"/lFm8gWgNoOq323vN9iZe15CVfo3.jpg"
      },
      {
         "cast_id":60,
         "character":"Businessman in Cab",
         "credit_id":"52fe425fc3a36847f801933f",
         "id":1164431,
         "name":"Winston May",
         "order":37,
         "profile_path":"/yk1uDQfU0s9l3GQ0f1ZHL7wijAq.jpg"
      },
      {
         "cast_id":61,
         "character":"Mayor's Aide",
         "credit_id":"52fe425fc3a36847f8019343",
         "id":159887,
         "name":"Tommy Hollis",
         "order":38,
         "profile_path":"/ugZxLJmrams2OVhtLRhSJV6oUL4.jpg"
      },
      {
         "cast_id":62,
         "character":"Louis's Neighbor (as Eda Reis Merin)",
         "credit_id":"52fe425fc3a36847f8019347",
         "id":140796,
         "name":"Eda Reiss Merin",
         "order":39,
         "profile_path":"/a3aiwg5H6l8vJYngEFdz5oDZ6gr.jpg"
      },
      {
         "cast_id":64,
         "character":"Policeman at Apartment (as Rick Mancini)",
         "credit_id":"52fe425fc3a36847f801934b",
         "id":157121,
         "name":"Ric Mancini",
         "order":39,
         "profile_path":"/y4tyCxv8pjmVJCCT73RGLBWIUfq.jpg"
      },
      {
         "cast_id":65,
         "character":"Reporter",
         "credit_id":"52fe425fc3a36847f801934f",
         "id":158810,
         "name":"Carol Ann Henry",
         "order":40,
         "profile_path":null
      },
      {
         "cast_id":66,
         "character":"Reporter (as James Hardy)",
         "credit_id":"52fe425fc3a36847f8019353",
         "id":175011,
         "name":"James Hardie",
         "order":41,
         "profile_path":null
      },
      {
         "cast_id":67,
         "character":"Reporter (as Frances Turner)",
         "credit_id":"52fe425fc3a36847f8019357",
         "id":116536,
         "name":"Frantz Turner",
         "order":42,
         "profile_path":"/bcU0XFsc5WNMnd60xLZav66vM8I.jpg"
      },
      {
         "cast_id":69,
         "character":"Ted Fleming",
         "credit_id":"52fe425fc3a36847f801935b",
         "id":1164434,
         "name":"Paul Trafas",
         "order":43,
         "profile_path":"/u51XPgFiQSr8as9PKb5lTxhl8mJ.jpg"
      },
      {
         "cast_id":70,
         "character":"Annette Fleming",
         "credit_id":"52fe425fc3a36847f801935f",
         "id":1164435,
         "name":"Cheryl Birchenfield",
         "order":44,
         "profile_path":"/yDbCBL4ZI5ZvLup1ZURcSbaBZM1.jpg"
      },
      {
         "cast_id":71,
         "character":"Dream Ghost (as Kym Herrin)",
         "credit_id":"52fe425fc3a36847f8019363",
         "id":175055,
         "name":"Kymberly Herrin",
         "order":45,
         "profile_path":"/70iXdFHy3oTSphXG8I0wEW6FXqy.jpg"
      },
      {
         "cast_id":75,
         "character":"Reporter",
         "credit_id":"52fe425fc3a36847f801936b",
         "id":129458,
         "name":"Stanley Grover",
         "order":47,
         "profile_path":"/ovPcrlDmfmVrl5qNNBtDXcSh4f3.jpg"
      },
      {
         "cast_id":76,
         "character":"Library Ghost",
         "credit_id":"52fe425fc3a36847f801936f",
         "id":1164436,
         "name":"Ruth Oliver",
         "order":48,
         "profile_path":"/zjUoYxWTk0EAOycu8mdSjx22weB.jpg"
      },
      {
         "cast_id":79,
         "character":"Reporter",
         "credit_id":"54ebb925c3a3686d56000034",
         "id":1430862,
         "name":"Nancy Kelly",
         "order":49,
         "profile_path":null
      }
   ],
   "crew":[
      {
         "credit_id":"52fe425fc3a36847f801922d",
         "department":"Directing",
         "id":8858,
         "job":"Director",
         "name":"Ivan Reitman",
         "profile_path":"/9XDAJ2VrY7pl7x08KScHpFepG7F.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f8019233",
         "department":"Writing",
         "id":707,
         "job":"Screenplay",
         "name":"Dan Aykroyd",
         "profile_path":"/h2PT9yZYv5ml5hL9jvCpWBTWgU.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f8019239",
         "department":"Writing",
         "id":1524,
         "job":"Screenplay",
         "name":"Harold Ramis",
         "profile_path":"/zLzR2UwzyrR9A6RMarZJV833X9K.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f801923f",
         "department":"Production",
         "id":8858,
         "job":"Producer",
         "name":"Ivan Reitman",
         "profile_path":"/9XDAJ2VrY7pl7x08KScHpFepG7F.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f8019245",
         "department":"Production",
         "id":8859,
         "job":"Executive Producer",
         "name":"Bernie Brillstein",
         "profile_path":"/mLFQdesNwMtvo3E64U2LnV8s4tG.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f801924b",
         "department":"Production",
         "id":8860,
         "job":"Producer",
         "name":"Michael C. Gross",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019251",
         "department":"Sound",
         "id":7182,
         "job":"Original Music Composer",
         "name":"Elmer Bernstein",
         "profile_path":"/3H3AqKesJnkwJzWFJY9hyFwci5m.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f8019257",
         "department":"Camera",
         "id":8862,
         "job":"Director of Photography",
         "name":"László Kovács",
         "profile_path":"/qvsRxyAPJw9Kv8ubV2AKWLiUcz8.jpg"
      },
      {
         "credit_id":"52fe425fc3a36847f801925d",
         "department":"Editing",
         "id":8863,
         "job":"Editor",
         "name":"David E. Blewitt",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019263",
         "department":"Editing",
         "id":7068,
         "job":"Editor",
         "name":"Sheldon Kahn",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019269",
         "department":"Production",
         "id":8864,
         "job":"Casting",
         "name":"Karen Rea",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f801926f",
         "department":"Art",
         "id":8867,
         "job":"Set Decoration",
         "name":"Marvin March",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019275",
         "department":"Costume & Make-Up",
         "id":8868,
         "job":"Costume Design",
         "name":"Theoni V. Aldredge",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f801927b",
         "department":"Costume & Make-Up",
         "id":8869,
         "job":"Costume Design",
         "name":"Suzy Benzinger",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019281",
         "department":"Costume & Make-Up",
         "id":8870,
         "job":"Makeup Artist",
         "name":"Leonard Engelman",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f8019287",
         "department":"Production",
         "id":8871,
         "job":"Production Manager",
         "name":"John G. Wilson",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f80192b1",
         "department":"Art",
         "id":12508,
         "job":"Other",
         "name":"Toto",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f80192b7",
         "department":"Production",
         "id":57601,
         "job":"Producer",
         "name":"Joe Medjuck",
         "profile_path":null
      },
      {
         "credit_id":"545cb134c3a3685350003546",
         "department":"Art",
         "id":1118257,
         "job":"Production Design",
         "name":"John DeCuir",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f80192cb",
         "department":"Art",
         "id":8866,
         "job":"Art Direction",
         "name":"John DeCuir Jr.",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f80192d1",
         "department":"Visual Effects",
         "id":21548,
         "job":"Visual Effects",
         "name":"Richard Edlund",
         "profile_path":null
      },
      {
         "credit_id":"52fe425fc3a36847f80192d7",
         "department":"Lighting",
         "id":1164426,
         "job":"Gaffer",
         "name":"Richmond L. Aguilar",
         "profile_path":null
      },
      {
         "credit_id":"55402c3b9251414af60005aa",
         "department":"Crew",
         "id":1395269,
         "job":"Visual Effects Art Director",
         "name":"John Bruno",
         "profile_path":null
      }
   ]
}
 */