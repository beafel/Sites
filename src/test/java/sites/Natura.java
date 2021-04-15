// Pacote
package sites;

//Bibliotecas

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

//Classes
public class Natura {

    //Atributos
    WebDriver driver;
    String url;
    String pastaPrint = "evidencias/" + new SimpleDateFormat("yyyy-MM-dd HH-mm").format(Calendar.getInstance().getTime()) + "/";

    //Metodos e Funcoes - Tirar Prints
    public void tirarPrint(String nomePrint) throws IOException {

        //variavel foto/ especializar Selenium para tirar foto/ gravar em arquivo/ nome do arquivo
        File foto = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(foto,new File(pastaPrint + nomePrint + ".png"));
    }

    @Before
    public void iniciar(){
        url = "https://natura.com.br";
        System.setProperty("webdriver.chrome.driver", "drivers/chrome/87/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    }

    @After
    public void finalizar(){
        driver.quit();
    }

    @Test
    public void consultaProdutoNatura() throws InterruptedException, IOException {
        driver.get(url);

        tirarPrint("Print 1 - Exibe Site da Natura");
        driver.findElement(By.cssSelector("span.SearchPreview_searchLabel__rS3z8")).click();
        driver.findElement(By.cssSelector("input.ComposedField_input__1zmI0")).sendKeys("KAIAK MASCULINO" + Keys.ENTER);
        tirarPrint("Passo 2 - Exibe pesquisa pelo produto KAIAK MASCULINO");

        //Validar produtos encontrados na pesquisa
        assertEquals("Kaiak Masculino", driver.findElement(By.cssSelector("span.SearchPreview_searchLabel__rS3z8")).getText());

        //fechar pop-up
        //driver.findElement(By.cssSelector("a.btClose")).click();

        driver.findElement(By.tagName("Kaiak Masculino")).click();
        tirarPrint("Passo 3 - Exibe a pagina do produto KAIAK MASCULINO");

        //Validar valor e parcelamento do produto
        assertEquals("R$ 121,90", driver.findElement(By.cssSelector("span.price-value")).getText());
        assertTrue(driver.findElement(By.cssSelector("InstallmentPhrase_phrase__1wrE8")).getText().contains("4x de R$ 30,48 sem juros"));
    }
}
