package link

import grails.test.*
import util.*
import org.codehaus.groovy.grails.commons.*

class Linkm2mIntegrationTests extends GrailsUnitTestCase {
	protected void setUp() {
		super.setUp()
		person=new Person().save()
		assertNotNull(person)
		tag=new Tag().save()
		assertNotNull(tag)
	}
	
	protected void tearDown() {
		person.delete()
		tag.delete()
		super.tearDown()
	}
	def check(Class domain,Class association,String setName,String name) {
		final DefaultGrailsDomainClass d=new DefaultGrailsDomainClass(domain)
		final GrailsDomainClassProperty p1=d.getPropertyByName(setName)
		println p1.dump()
		assert p1
		assert p1.oneToMany
		assert p1.bidirectional
		assert p1.referencedPropertyType==association
		assert p1.referencedPropertyName==name
	}
	void test1() {
		check(Person.class,FavoriteTag.class,'favoriteTags','person')
	}
	void testLinkWithAllArgs() {
		final Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class,'favoriteTags','favoriteTags','person','tag')
		final a=linkm2m.link(person,tag)
		final founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		linkm2m.unlink(person,tag)
		final foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	}
	void testLinkWithSomeArgs() {
		Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class,'favoriteTags','favoriteTags')
		final a=linkm2m.link(person,tag)
		final founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		linkm2m.unlink(person,tag)
		final foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	}
	void testLinkWithFewest() {
		Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class)
		final a=linkm2m.link(person,tag)
		final founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		linkm2m.unlink(person,tag)
		final foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	}
	void testLinkWithNullClosure() {
		Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class)
		final a=linkm2m.link(person,tag,null)
		final founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		linkm2m.unlink(person,tag)
		final foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	}
	void testLinkWithClosure() {
		Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class)
		final a=linkm2m.link(person,tag) { it->
			assertNotNull it
			assertTrue FavoriteTag.isInstance(it)
		}
		final founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		linkm2m.unlink(person,tag) { it->
			assertNotNull it
			assertTrue FavoriteTag.isInstance(it)
		}
		final foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	}
    void testLinkWithInheritance() {
    	final Person vip=new Vip().save()
    	assertNotNull vip
		final Linkm2m linkm2m=new Linkm2m(Person.class,Tag.class,FavoriteTag.class)
		final a=linkm2m.link(vip,tag)
		final founda=FavoriteTag.findByPersonAndTag(vip,tag)
		assertNotNull(founda)
		linkm2m.unlink(vip,tag)
		final foundagain=FavoriteTag.findByPersonAndTag(vip,tag)
		assertNull(foundagain)
		vip.delete()
	    }
	Person person
	Tag tag
}
