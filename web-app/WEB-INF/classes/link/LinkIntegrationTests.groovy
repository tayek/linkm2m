package link

import grails.test.*
import static util.Util.*

class LinkIntegrationTests extends GrailsUnitTestCase {
	Person person
	Tag tag
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

    void testLink() {
		def a=link(Person.class,Tag.class,FavoriteTag.class,person,tag,'favoriteTags','favoriteTags','person','tag')
		def founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		unlink(Person.class,Tag.class,FavoriteTag.class,person,tag,'favoriteTags','favoriteTags','person','tag')
		def foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	    }
    void testLink2() {
		def a=link(Person.class,Tag.class,FavoriteTag.class,person,tag,'favoriteTags','favoriteTags')
		def founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		unlink(Person.class,Tag.class,FavoriteTag.class,person,tag,'favoriteTags','favoriteTags')
		def foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	    }
    void testLink3() {
		def a=link(Person.class,Tag.class,FavoriteTag.class,person,tag)
		def founda=FavoriteTag.findByPersonAndTag(person,tag)
		assertNotNull(founda)
		unlink(Person.class,Tag.class,FavoriteTag.class,person,tag)
		def foundagain=FavoriteTag.findByPersonAndTag(person,tag)
		assertNull(foundagain)
	    }
}
