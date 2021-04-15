package sites;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class) // massa de teste
public class PesquisaGoogle {
    WebDriver driver;
    static String url;
    String pastaPrint = "evidencias/" + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime()) + "/";


    // 3.2- Metodos(nao tem retorno) ou Funcoes(tem retorno) = o que a classe sabe fazer (verbos)

    //metodos ou funcoes de apoio (util ou commons)

    // metodo para tirar prints

    public void tirarPrint(String nomePrint) throws IOException {
        // variavel foto do tipo arquivo que vai receber o resultado do tipo selenium da imagem atual
        File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); // especializacao do Selenium tirar foto, fotografia com saida para um arquivo
        FileUtils.copyFile(foto,new File(pastaPrint + nomePrint + ".png")); // grava o print da tela no arquivo

    }

// Funcao para ler uma massa de teste

    // 1 - Atributos
    private String id;
    private String produto;
    private String browser;

    // 2 -  Construtor (De-Para entre os campos na massa de os atributos de cima)
    public PesquisaGoogle(String id, String produto, String browser) {
        this.id = id;
        this.produto = produto;
        this.browser = browser;
    }

    // 3 - Collection Intermediaria entre o Constructor e a Collection que vai fazer a leitura
    @Parameterized.Parameters
    public static Collection<String[]> LerArquivo() throws IOException {
        return LerCSV("db/massa_de_teste_Google.csv");
    }

    // 4 - Collection que le um arquivo .csv
    public static Collection<String[]> LerCSV(String nomeCSV) throws IOException {
        BufferedReader arquivo = new BufferedReader(new FileReader(nomeCSV)); //le o arquivo no disco e disponibiliza na memoria RAM
        String linha;
        List<String[]> dados = new ArrayList<>(); //cria uma lista para receber o resultado

        while ((linha = arquivo.readLine()) != null) {
            String[] campos = linha.split(";"); // campos ? uma sequencia de strings, por isso ? entre [], seria o Vetor
            dados.add(campos);
        }
        arquivo.close();
        return dados;
    }

    @BeforeClass
    public static void antesDeTudo(){
        url = "https://www.google.com.br/";
        System.setProperty("webdriver.chrome.driver","drivers/chrome/88/chromedriver.exe");
        System.setProperty("webdriver.edge.driver","drivers/edge/msedgedriver.exe");
    }

    @Before
    public void iniciar(){
        switch (browser){
            case "Chrome":
                driver = new ChromeDriver();
                break;
            case "Edge":
                driver = new EdgeDriver();
                break;
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    }

    @After
    public void finalizar(){
        driver.quit();
    }

    @Test
    public void pesquisaProduto() throws InterruptedException, IOException {
        driver.get(url);

        tirarPrint("Passo 1 - Acessou o Google");

        driver.findElement(By.xpath("//input[@name='q']")).sendKeys(produto + Keys.ENTER);
        Thread.sleep(3000);

        tirarPrint("Passo 2 - Exibe os resultados encontados relacionados à pesquisa");

        // Retorna o titulo da pagina
        driver.getTitle();

        // Valida a pesquisa
        assertEquals("Anúncios·",driver.findElement(By.cssSelector("span.dH53Z.VqFMTc.p8AiDd")).getText());
        assertTrue(driver.findElement(By.cssSelector("span.N6sL8d")).getText().contains(produto));
    }
}
