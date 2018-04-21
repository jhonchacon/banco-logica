package co.edu.usbcali.banco.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import co.edu.usbcali.banco.modelo.Cliente;
import co.edu.usbcali.banco.modelo.TipoDocumento;

class TestCliente {

	//Linea para utilizar log for java se debe utilizar slf4 tipo factory 
	//para poder cambiar el motor de log cuando se requiera
	private final static Logger log=LoggerFactory.getLogger(TestCliente.class);
	
	static EntityManagerFactory entityManagerFactory=null;
	static EntityManager entityManager =null;
		
	BigDecimal clieId=new BigDecimal(142020);
	
	
	@Test
	@DisplayName("CreaCliente")
	void atest() {
		/*
		 * log for java, es un sistema de logger, se puede enviar a cualquier base de datos, archivos, mensajes
		 * consola, archivo etc..
		 * se utiliza debug, info,trace etc..
		
		log.info("Se ejecuto la prueba A");
		*/
		assertNotNull(entityManager, "El entity manager es nulo");
		Cliente cliente = entityManager.find(Cliente.class, clieId);
		assertNull(cliente, "El cliente ya existe" );
		cliente=new Cliente();
		
		//se inyecta el codigo que se va a insertar en la persistencia
		cliente.setActivo('S');
		cliente.setClieId(clieId);
		cliente.setDireccion("avenida 3an 26n 83");
		cliente.setEmail("jjc@gmail.com");
		cliente.setNombre("Jhon Simpson");
		cliente.setTelefono("555 555 5555");
		
		//cuando el objeto a insertar tiene constraint en la base de datos, se debe encontrar primero
		TipoDocumento tipoDocumento=entityManager.find(TipoDocumento.class, new Long(2));
		assertNotNull(tipoDocumento, "El tipo de documento no existe");
		cliente.setTipoDocumento(tipoDocumento);
		
		
		//Enviar datos a la base de datos
		entityManager.getTransaction().begin();
		entityManager.persist(cliente);
		entityManager.getTransaction().commit();
		
	}

	@Test
	@DisplayName("ConsultarClienteporId")
	void btest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Cliente cliente = entityManager.find(Cliente.class, clieId);
		assertNotNull(cliente, "El cliente No existe" );

		log.info("Id:" + cliente.getClieId());
		log.info("Nombre:" + cliente.getNombre());
		
	}
	
	@Test
	@DisplayName("ModificaCliente")
	void ctest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Cliente cliente = entityManager.find(Cliente.class, clieId);
		assertNotNull(cliente, "El cliente no existe" );
		cliente.setActivo('N');
		cliente.setDireccion("Avenida 3an 26n 83 - Nuevo");
		
		TipoDocumento tipoDocumento=entityManager.find(TipoDocumento.class, new Long(2));
		assertNotNull(tipoDocumento, "El tipo de documento no existe");
		cliente.setTipoDocumento(tipoDocumento);
		
		
		//Enviar datos a la base de datos
		entityManager.getTransaction().begin();
		entityManager.merge(cliente);
		entityManager.getTransaction().commit();
		
	}	
	
	@Test
	@DisplayName("EliminarCliente")
	void dtest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Cliente cliente = entityManager.find(Cliente.class, clieId);
		assertNotNull(cliente, "El cliente no existe" );
		
		//Enviar datos a la base de datos
		entityManager.getTransaction().begin();
		entityManager.remove(cliente);
		entityManager.getTransaction().commit();
		
	}	
		
	@Test
	@DisplayName("ConsultarClientes")
	void etest() {
		assertNotNull(entityManager, "El entity manager es nulo");

		String jpql="select cli from Cliente cli";
		List<Cliente> losClientes = entityManager.createQuery(jpql).getResultList();
		
		//expresion lambda, variable anonima para escribir en una l�nea las comparaciones
		losClientes.forEach(cliente->{
			log.info("ID:" +cliente.getClieId());
			log.info("Nombre" + cliente.getNombre());
		});
		
		/*
		//recorrido tradicional con for
		for (Cliente cliente : losClientes) {
			log.info("ID:" +cliente.getClieId());
			log.info("Nombre" + cliente.getNombre());
			
		}
		*/
		
		
	}	
			
	
	
	@AfterEach
	public void iniciar_todo() {
		log.info("ejecuto @afterEach");
	}

	//no puede ser static//
	@BeforeEach
	public void finalizar_todo() {
		log.info("ejecuto @BeforeEach");
	}
	
	@BeforeAll
	public static void iniciar() {
		log.info("Ejecuto el @BeforeAll");
		entityManagerFactory=Persistence.createEntityManagerFactory("banco-logica");
		entityManager=entityManagerFactory.createEntityManager();
	}
	
	@AfterAll
	public static void finalizar() {
		log.info("Se ejecuto el @AfterAll");
		entityManager.close();
		entityManagerFactory.close();
	}
	
	

}
