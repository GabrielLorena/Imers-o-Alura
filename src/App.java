import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws Exception {

        // fazer uma conexão HTTP e buscar os top 250 filmes
        String url = "https://raw.githubusercontent.com/alura-cursos/imersao-java-2-api/main/TopMovies.json";
        URI endereco = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();

        // pegar só os dados que interessam (título, poster, classificação)
        JsonParser parser = new JsonParser();
        List<Map<String, String>> listaDeFilme = parser.parse(body);
        
        var diretorio = new File("figurinhas/");
        diretorio.mkdir();

        // exibir e manipular os dados
        var geradora = new GeradorDeFigurinhas();
        
        for (Map<String,String> filme: listaDeFilme) {

            String urlImagem = filme.get("image");
            String urlImagemMaior = urlImagem.replaceFirst("(@?\\.)([0-9A-Z,_]+).jpg$", "$1.jpg");
            String titulo = filme.get("title");
            double classificacao = Double.parseDouble(filme.get("imDbRating"));

            String textoFigurinha;
            InputStream imagemDog;
            if (classificacao >= 8.0) {
                textoFigurinha = "TOPZERA";
                imagemDog = new FileInputStream(new File("sobreposicao/empolgado.jpg"));
            } else {
                textoFigurinha = "HMMMM...";
                imagemDog = new FileInputStream(new File("sobreposicao/desconfiado.jpg"));
            }


            InputStream inputStream = new URL(urlImagemMaior).openStream();
            String nomeArquivo = "figurinhas/" + titulo + ".png";
            

            geradora.cria(inputStream, nomeArquivo, textoFigurinha, imagemDog);

            System.out.println(titulo);
            System.out.println();
        }
            }
        }
