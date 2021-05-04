package sites;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Boticario {
    String url = "https://www.boticario.com.br";
    WebDriver driver;
    String pastaPrint = "evidencias/" + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime()) + "/";

    public void tirarPrint(String nomePrint) throws IOException {
        // variavel foto do tipo arquivo que vai receber o resultado do tipo selenium da imagem atual
        File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE); // especializacao do Selenium tirar foto, fotografia com saida para um arquivo
        FileUtils.copyFile(foto,new File(pastaPrint + nomePrint + ".png")); // grava o print da tela no arquivo
    }

    @Before
    public void iniciar(){

        ChromeOptions chOptions = new ChromeOptions(); // instanciar o objeto de configuracao do ChromeDriver
        chOptions.addArguments("--disable-notifications");

        System.setProperty("webdriver.chrome.driver","drivers/chrome/90/chromedriver90.exe");
        driver = new ChromeDriver(chOptions); // <-- Instanciar o Selenium como um controlador do Chrome
        driver.manage().window().maximize();
    }

    @After
    public void finalizar(){
        driver.quit();
    }

    @Test
    public void consultarProdutoBoticario() throws IOException {
        driver.get(url);
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        //todo: fazer o 2o popup (do browser)
        //driver.findElement(By.id("onesignal-slidedown-cancel-button")).click(); --> não precisa, passou com a configuração chOptions

        tirarPrint("Print 1 - Exibe Site do Boticario");

        driver.findElement(By.id("autocomplete-input")).sendKeys("Quasar Graffiti Colonia" + Keys.ENTER);
        assertEquals("Resultado de busca para \"Quasar Graffiti Colonia\"", driver.findElement(By.cssSelector("h1.content-page-title.content-page-title-list")).getText());

        tirarPrint("Print 2 - Exibe resultado da busca por produto");

        driver.findElement(By.cssSelector("a[href^='https://www.boticario.com.br/quasar-graffiti-desodorante-colonia-100-ml/']")).click();

        assertTrue(driver.findElement(By.cssSelector("div.nproduct-header")).getText().contains("Quasar Graffiti Desodorante"));
        assertEquals("R$ 114,90", driver.findElement(By.cssSelector("div.nproduct-price-value")).getText());
        assertEquals("5x de R$ 22,98", driver.findElement(By.cssSelector("div.nproduct-price-installments")).getText());

        tirarPrint("Print 3 - Exibe a pagina do produto");
    }
}
