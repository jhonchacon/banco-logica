package co.edu.usbcali.banco.jpa;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import co.edu.usbcali.banco.modelo.Usuario;
import co.edu.usbcali.banco.modelo.TipoUsuario;

public class TestUsuario {
	private final static Logger log=LoggerFactory.getLogger(TestUsuario.class);
	
	static EntityManagerFactory entityManagerFactory=null;
	static EntityManager entityManager =null;
		
	String usrNom=new String("jjchacon");
	BigDecimal usrId=new BigDecimal(9876);
	
	@Test
	@DisplayName("CreaUsuario")
	void atest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Usuario usuario = entityManager.find(Usuario.class, usrNom);
		assertNull(usuario, "El Usuario ya existe" );
		usuario=new Usuario();
		
		usuario.setUsuUsuario(usrNom);
		usuario.setIdentificacion(usrId);
		usuario.setActivo('S');
		usuario.setClave("9876");
		usuario.setNombre("Jhon Simpson");
		
		//cuando el objeto a insertar tiene constraint en la base de datos, se debe encontrar primero
		TipoUsuario tipoUsuario=entityManager.find(TipoUsuario.class, new Long(2));
		assertNotNull(tipoUsuario, "El tipo de usuario no existe");
		usuario.setTipoUsuario(tipoUsuario);
		
		
		//Enviar datos a la base de datos
		entityManager.getTransaction().begin();
		entityManager.persist(usuario);
		entityManager.getTransaction().commit();
		
	}

	@Test
	@DisplayName("ConsultarUsuarioporId")
	void btest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Usuario usuario = entityManager.find(Usuario.class, usrNom);
		assertNotNull(usuario, "El Usuario No existe" );

		log.info("Id:" + usuario.getUsuUsuario());
		log.info("Nombre:" + usuario.getNombre());
		
	}
	
	@Test
	@DisplayName("ModificaUsuario")
	void ctest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Usuario usuario = entityManager.find(Usuario.class, usrNom);
		assertNotNull(usuario, "El Usuario no existe" );
		usuario.setActivo('N');
		usuario.setClave("catalina");
		
		TipoUsuario tipoUsuario=entityManager.find(TipoUsuario.class, new Long(2));
		assertNotNull(tipoUsuario, "El tipo de documento no existe");
		usuario.setTipoUsuario(tipoUsuario);
		
		entityManager.getTransaction().begin();
		entityManager.merge(usuario);
		entityManager.getTransaction().commit();
		
	}	
	
	@Test
	@DisplayName("EliminarUsuario")
	void dtest() {
		assertNotNull(entityManager, "El entity manager es nulo");
		Usuario usuario = entityManager.find(Usuario.class, usrNom);
		assertNotNull(usuario, "El Usuario no existe" );
		
		//Enviar datos a la base de datos
		entityManager.getTransaction().begin();
		entityManager.remove(usuario);
		entityManager.getTransaction().commit();
		
	}	
		
	@Test
	@DisplayName("ConsultarUsuarios")
	void etest() {
		assertNotNull(entityManager, "El entity manager es nulo");

		String jpql="select usu from Usuario usu";
		List<Usuario> losUsuarios = entityManager.createQuery(jpql).getResultList();
		
		//expresion lambda, variable anonima para escribir en una l�nea las comparaciones
		losUsuarios.forEach(Usuario->{
			log.info("ID:" +Usuario.getUsuUsuario());
			log.info("Nombre" + Usuario.getNombre());
		});
		
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
