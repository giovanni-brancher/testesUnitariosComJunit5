package br.com.caelum.leilao.dominio;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.servico.Avaliador;

@RunWith(JUnitPlatform.class)
public class LeilaoTest {
	
	private Usuario alex;
	private Usuario melissa;
	private Usuario carlos;
	private Usuario roger;
	
	@BeforeEach
	public void beforeEach() {
		new Avaliador();
		this.alex = new Usuario("Alex");
		this.melissa = new Usuario("Melissa");
		this.carlos = new Usuario("Carlos");
		this.roger = new Usuario("Roger");
	}

	@Test
	public void deveReceberUmLance() {
		Leilao leilao = new CriadorDeLeilao("Ferrari")
				.lance(this.alex, 1000000.00)
				.constroi();
		
		assertAll(
			() -> assertEquals(1, leilao.getLances().size()),
			() -> assertEquals(1000000.00, leilao.getLances().get(0).getValor())
		);
	}
	
	@Test
	public void deveReceberMaisDeUmLance() {
		Leilao leilao = new CriadorDeLeilao("Caixa de bombom")
				.lance(this.alex, 50.00)
				.lance(this.melissa, 60.00)
				.lance(this.carlos, 62.00)
				.lance(this.roger, 70.00)
				.constroi();
		
		assertAll(
			() -> assertEquals(4, leilao.getLances().size()),
			() -> assertEquals(50.00, leilao.getLances().get(0).getValor()),
			() -> assertEquals(60.00, leilao.getLances().get(1).getValor()),
			() -> assertEquals(62.00, leilao.getLances().get(2).getValor()),
			() -> assertEquals(70.00, leilao.getLances().get(3).getValor())
		);
	}
	
	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		Leilao leilao = new CriadorDeLeilao("Caixa de bombom")
				.lance(this.carlos, 50.00)
				.lance(this.carlos, 60.00)
				.constroi();
		
		assertAll(
			() -> assertEquals(1, leilao.getLances().size()),
			() -> assertEquals(50.00, leilao.getLances().get(0).getValor(), 0.00001)
		);
	}
	
	@Test
	public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario() {
		Leilao leilao = new Leilao("Sorvete");
		
		Usuario joaquim = new Usuario("Joaquim");
		Usuario lais = new Usuario("Laís");
		Usuario pedro = new Usuario("Pedro");
		
		leilao.propoe(new Lance(joaquim, 1.00)); // primeiro
		leilao.propoe(new Lance(lais, 1.02));
		leilao.propoe(new Lance(joaquim, 1.05)); // segundo
		leilao.propoe(new Lance(pedro, 1.07));
		leilao.propoe(new Lance(joaquim, 1.08)); // terceiro
		leilao.propoe(new Lance(pedro, 1.09));
		leilao.propoe(new Lance(joaquim, 1.10)); // quarto
		leilao.propoe(new Lance(lais, 1.12));
		leilao.propoe(new Lance(joaquim, 1.15)); // quinto
		leilao.propoe(new Lance(lais, 1.17));
		leilao.propoe(new Lance(joaquim, 1.20)); // sexto
		
		List<Lance> lancesJoaquim = leilao.getLancesUsuario(joaquim);
		
		assertAll(
			() -> assertEquals(5, lancesJoaquim.size())
		);
	}
	
	@Test
	public void naoDeveAceitarMaisDoQue5LancesDeUmMesmoUsuario_Versao2() {
		Leilao leilao = new CriadorDeLeilao("Sorvete")
				.lance(this.melissa, 1.00)
				.lance(this.roger, 1.02)
				.lance(this.melissa, 1.05)
				.lance(this.roger, 1.07)
				.lance(this.melissa, 1.08)
				.lance(this.roger, 1.09)
				.lance(this.melissa, 1.10)
				.lance(this.roger, 1.12)
				.lance(this.melissa, 1.15)
				.lance(this.roger, 1.17)
				.lance(this.melissa, 1.20) // sexto lance de Melissa.
				.constroi();
		
		List<Lance> lancesMelissa = leilao.getLancesUsuario(this.melissa);
		
		assertAll(
			() -> assertEquals(5, lancesMelissa.size()),
			() -> assertEquals(1.15, lancesMelissa.get(lancesMelissa.size() - 1).getValor())
		);
	}
	
	@Test
	public void deveDobrarUltimoLanceDoUsuario() {
		Leilao leilao = new Leilao("Patinete");
		
		Usuario mariaLuiza = new Usuario("Maria Luiza");
		Usuario luisFelipe = new Usuario("Luis Felipe");
		
		leilao.propoe(new Lance(mariaLuiza, 100.00));
		leilao.propoe(new Lance(luisFelipe, 120.00));
		leilao.dobreLance(mariaLuiza);
		
		assertAll(
			() -> assertEquals(3, leilao.getLances().size()),
			() -> assertEquals(200.00, leilao.getLances().get(2).getValor(), 0.00001)
		);
	}
	
	@Test
	public void naoDeveDobrarUltimoLanceDoUsuario() {
		Leilao leilao = new Leilao("Patinete");
		
		Usuario mariaLuiza = new Usuario("Maria Luiza");
		Usuario luisFelipe = new Usuario("Luis Felipe");
		
		leilao.propoe(new Lance(luisFelipe, 120.00)); // válido
		leilao.dobreLance(mariaLuiza); // inválido
		
		assertAll(
			() -> assertEquals(1, leilao.getLances().size()),
			() -> assertEquals(120.00, obterUltimoLance(leilao).getValor(), 0.00001)
		);
	}
	
	@Test
	public void deveDobrarQuartoLanceDoUsuario() {
		Leilao leilao = new Leilao("Patinete");
		
		Usuario mariaLuiza = new Usuario("Maria Luiza");
		Usuario luisFelipe = new Usuario("Luis Felipe");
		
		leilao.propoe(new Lance(mariaLuiza, 100.00));
		leilao.propoe(new Lance(luisFelipe, 120.00));
		leilao.propoe(new Lance(mariaLuiza, 130.00));
		leilao.propoe(new Lance(luisFelipe, 150.00));
		leilao.propoe(new Lance(mariaLuiza, 170.00));
		leilao.propoe(new Lance(luisFelipe, 180.00));
		leilao.propoe(new Lance(mariaLuiza, 190.00));
		leilao.propoe(new Lance(luisFelipe, 280.00));
		
		leilao.dobreLance(mariaLuiza);
		
		assertAll(
			() -> assertEquals(9, leilao.getLances().size()),
			() -> assertEquals(380.00, obterUltimoLance(leilao).getValor(), 0.00001)
		);
	}
	
	@Test
	public void naoDeveDobrarQuintoLanceDoUsuario() {
		Leilao leilao = new Leilao("Patinete");
		
		Usuario mariaLuiza = new Usuario("Maria Luiza");
		Usuario luisFelipe = new Usuario("Luis Felipe");
		
		leilao.propoe(new Lance(mariaLuiza, 100.00));
		leilao.propoe(new Lance(luisFelipe, 120.00));
		leilao.propoe(new Lance(mariaLuiza, 130.00));
		leilao.propoe(new Lance(luisFelipe, 150.00));
		leilao.propoe(new Lance(mariaLuiza, 170.00));
		leilao.propoe(new Lance(luisFelipe, 180.00));
		leilao.propoe(new Lance(mariaLuiza, 190.00));
		leilao.propoe(new Lance(luisFelipe, 200.00));
		leilao.propoe(new Lance(mariaLuiza, 250.00));
		leilao.propoe(new Lance(luisFelipe, 280.00));
		
		leilao.dobreLance(mariaLuiza); // inválido
		
		assertAll(
			() -> assertEquals(10, leilao.getLances().size()),
			() -> assertEquals(280.00, obterUltimoLance(leilao).getValor(), 0.00001)
		);
	}

	private Lance obterUltimoLance(Leilao leilao) {
		return leilao.getLances().get(leilao.getLances().size()-1);
	}
}
