package br.com.caelum.leilao.servico;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AvaliadorTest {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario maria;
	private Usuario pedro;
	private Usuario ana;
	
	@Before
	public void before() {}
	
	@After
	public void after() {}

	@BeforeEach
	public void beforeEach() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.maria = new Usuario("Maria");
		this.pedro = new Usuario("Pedro");
		this.ana = new Usuario("Ana");
	}
	
	@AfterEach
	public void afterEach() {}
	
	@Test
	@Order(0)
	@Tag("leiloeiro")
	@DisplayName("O leiloeiro não deve avaliar um leilão sem lances.")
	public void naoDeveAvaliarUmLeilaoSemLancesTest() {
		Leilao leilao = new CriadorDeLeilao("Notebook").constroi();
		
		assertThrows(RuntimeException.class, () -> {
			this.leiloeiro.avalia(leilao);
	    });
	}

	@Test
	@Order(1)
	@Tag("lance")
	public void deveObterLanceComMaiorValorTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));
		leilao.propoe(new Lance(maria, 3250.00));
		leilao.propoe(new Lance(pedro, 3900.00));

		this.leiloeiro.avalia(leilao);

		assertEquals(4000.00, leiloeiro.getMaiorLance());
	}

	@Test
	@Order(2)
	@Tag("lance")
	public void deveObterLanceComMenorValorTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));
		leilao.propoe(new Lance(maria, 3250.00));
		leilao.propoe(new Lance(pedro, 3900.00));

		this.leiloeiro.avalia(leilao);

		assertEquals(3250.00, leiloeiro.getMenorLance());
	}

	@Test
	@Order(3)
	@Tag("lance")
	public void deveObterValorMedioLancesTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));
		leilao.propoe(new Lance(maria, 3250.00));
		leilao.propoe(new Lance(pedro, 3900.00));

		this.leiloeiro.avalia(leilao);

		assertEquals(3716.66, leiloeiro.getMediaDosLances(), 0.01);
	}

	@Test
	@Order(4)
	@Tag("lance")
	public void deveObterApenasUmLanceTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));

		this.leiloeiro.avalia(leilao);

		assertEquals(4000.00, leiloeiro.getMaiorLance(), 0.001);
		assertEquals(4000.00, leiloeiro.getMenorLance(), 0.001);
		assertEquals(4000.00, leiloeiro.getMediaDosLances(), 0.001);
	}

	@Test
	@Order(5)
	@Tag("lance")
	public void deveObterOsTresMaioresLancesTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));
		leilao.propoe(new Lance(maria, 5000.00));
		leilao.propoe(new Lance(maria, 3250.00));
		leilao.propoe(new Lance(pedro, 3900.00));
		leilao.propoe(new Lance(pedro, 3500.00));

		this.leiloeiro.avalia(leilao);

		List<Lance> lances = leiloeiro.getMaioresLances();
		assertTrue("Deveria conter 3 lances.", lances.size() == 3);
		assertEquals(5000.00, lances.get(0).getValor(), 0.001);
		assertEquals(4000.00, lances.get(1).getValor(), 0.001);
		assertEquals(3900.00, lances.get(2).getValor(), 0.001);
	}

	@Test
	@Order(6)
	@Tag("lance")
	public void deveObterOMaiorLanceUnicoTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));

		this.leiloeiro.avalia(leilao);

		List<Lance> lances = leiloeiro.getMaioresLances();
		assertTrue("Deveria conter 1 lance.", lances.size() == 1);
		assertEquals(4000.00, lances.get(0).getValor(), 0.001);
	}

	@Test
	@Order(7)
	@Tag("lance")
	public void deveObterOsMaioresLancesComDoisLancesApenasTest() {
		Leilao leilao = new Leilao("Playstation 5");
		leilao.propoe(new Lance(joao, 4000.00));
		leilao.propoe(new Lance(maria, 4200.00));

		this.leiloeiro.avalia(leilao);

		List<Lance> lances = leiloeiro.getMaioresLances();
		assertTrue("Deveria conter 2 lances.", lances.size() == 2);
		assertEquals(4200.00, lances.get(0).getValor(), 0.001);
		assertEquals(4000.00, lances.get(1).getValor(), 0.001);
	}

	@Test
	@Order(8)
	@Tag("lance")
	public void deveObterNenhumLanceTest() {
		Leilao leilao = new CriadorDeLeilao("Playstation 5").constroi();
		List<Lance> lances = leilao.getLances();

		assertAll("Lista vazia lances", 
				() -> assertTrue("Deveria conter 0 lances.", lances.size() == 0),
				() -> assertEquals(Double.NEGATIVE_INFINITY, leiloeiro.getMaiorLance()),
				() -> assertEquals(Double.POSITIVE_INFINITY, leiloeiro.getMenorLance()),
				() -> assertEquals(0.00, leiloeiro.getMediaDosLances()));
	}

	@Test
	@Order(9)
	@Tag("lance")
	public void deveObterMaiorMenorValorLancesAleatoriosTest() {
		Leilao leilao = new Leilao("Casa na praia");
		leilao.propoe(new Lance(ana, 500000.00));
		leilao.propoe(new Lance(maria, 475850.47));
		leilao.propoe(new Lance(pedro, 505800.50));
		leilao.propoe(new Lance(joao, 498001.00));
		leilao.propoe(new Lance(maria, 550000.11));
		leilao.propoe(new Lance(ana, 550000.00));
		leilao.propoe(new Lance(pedro, 560501.00));
		leilao.propoe(new Lance(joao, 542010.80));
		leilao.propoe(new Lance(ana, 320000.00));
		leilao.propoe(new Lance(maria, 375000.55));

		this.leiloeiro.avalia(leilao);

		assertEquals(560501.00, leiloeiro.getMaiorLance(), 0.001);
		assertEquals(320000.00, leiloeiro.getMenorLance(), 0.001);
	}

	@Test
	@Order(10)
	@Tag("lance")
	public void deveObterMaiorMenorValorLancesDecrescentesTest() {
		Leilao leilao = new Leilao("Casa na praia");
		leilao.propoe(new Lance(ana, 50000.00));
		leilao.propoe(new Lance(maria, 48000.47));
		leilao.propoe(new Lance(pedro, 45000.50));
		leilao.propoe(new Lance(joao, 42000.00));
		leilao.propoe(new Lance(maria, 40000.11));
		leilao.propoe(new Lance(ana, 39000.00));
		leilao.propoe(new Lance(pedro, 38000.00));
		leilao.propoe(new Lance(joao, 37000.80));
		leilao.propoe(new Lance(ana, 36000.00));
		leilao.propoe(new Lance(maria, 35500.55));

		this.leiloeiro.avalia(leilao);

		assertEquals(50000.00, leiloeiro.getMaiorLance(), 0.00001);
		assertEquals(35500.55, leiloeiro.getMenorLance(), 0.00001);
	}

	@Test
	@Order(11)
	@Tag("lance")
	public void deveObterMaiorMenorValorLancesCrescentesTest() {
		Leilao leilao = new Leilao("Casa na praia");
		leilao.propoe(new Lance(ana, 1000.00));
		leilao.propoe(new Lance(maria, 2000.47));
		leilao.propoe(new Lance(pedro, 3000.50));
		leilao.propoe(new Lance(joao, 4500.00));
		leilao.propoe(new Lance(maria, 5000.11));
		leilao.propoe(new Lance(ana, 6000.00));

		this.leiloeiro.avalia(leilao);

		assertAll("Validações MAIOR e MENOR lance",
				() -> assertEquals(6000.00, leiloeiro.getMaiorLance(), 0.00001),
				() -> assertEquals(1000.00, leiloeiro.getMenorLance(), 0.00001));

		// assertEquals(6000.00, leiloeiro.getMaiorLance(), 0.001);
		// assertEquals(1000.00, leiloeiro.getMenorLance(), 0.001);
	}
	
	@Test
	@Order(12)
	@Tag("lance")
	public void deveObterMaiorMenorValorLancesCrescentesVersao2Test() {
		Leilao leilao = new CriadorDeLeilao("Casa na praia")
				.lance(ana, 1000.00)
				.lance(maria, 2000.00)
				.lance(pedro, 3000.00)
				.lance(joao, 4000.00)
				.lance(maria, 5000.00)
				.lance(ana, 6000.00)
				.constroi();

		this.leiloeiro.avalia(leilao);
		
		assertAll("Validações MAIOR e MENOR lance",
				() -> assertEquals(6000.00, leiloeiro.getMaiorLance(), 0.00001),
				() -> assertEquals(1000.00, leiloeiro.getMenorLance(), 0.00001),
				// Alternativa com o framework Hancrest.
				() -> assertThat(leiloeiro.getMaiorLance(), equalTo(6000.00)));
	}
}
