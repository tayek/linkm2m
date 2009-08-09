package util
import org.codehaus.groovy.grails.commons.*
public class Util{
	static String f2u(String s) { /* first to uppercase */
		s[0].toUpperCase()+s.substring(1) 
	}
	static String f2l(String s) { /* first to lowercase */
		s[0].toLowerCase()+s.substring(1) 
	}
	static String plural(String string) { 
		string+'s' 
	}
	static String shortName(Class clazz) {
		clazz.name.split(/\./)[-1] // fragile!
	}
	static void sanityCheck(Class owner1,Class owner2,Class association,
			Object instance1,Object instance2,
			String setName1,String setName2,
			String name1,String name2) {
		assert owner1.isInstance(instance1)
		assert owner2.isInstance(instance2)
		DefaultGrailsDomainClass owner1Domain=new DefaultGrailsDomainClass(owner1)
		GrailsDomainClassProperty p1=owner1Domain.getPropertyByName(setName1)
		assert p1
		assert p1.oneToMany
		assert p1.bidirectional
		assert p1.referencedPropertyType==association
		assert p1.referencedPropertyName==name1
		DefaultGrailsDomainClass owner2Domain=new DefaultGrailsDomainClass(owner2)
		GrailsDomainClassProperty p2=owner2Domain.getPropertyByName(setName2)
		assert p2
		assert p2.oneToMany
		assert p2.bidirectional
		assert p2.referencedPropertyType==association
		assert p2.referencedPropertyName==name2
		DefaultGrailsDomainClass associationDomain=new DefaultGrailsDomainClass(association)
		GrailsDomainClassProperty p3=associationDomain.getPropertyByName(name1)
		assert p3
		assert p3.manyToOne
		assert p3.bidirectional
		assert p3.referencedPropertyType==owner1
		assert p3.referencedPropertyName==setName1
		GrailsDomainClassProperty p4=associationDomain.getPropertyByName(name2)
		assert p4
		assert p4.manyToOne
		assert p4.bidirectional
		assert p4.referencedPropertyType==owner2
		assert p4.referencedPropertyName==setName2
	}

	static Object link(Class owner1,Class owner2,Class association,
			Object instance1,Object instance2,
			String setName1=f2l(plural(shortName(association))),String setName2=f2l(plural(shortName(association))),
			String name1=f2l(shortName(owner1)),String name2=f2l(shortName(owner2))) {
		sanityCheck(owner1,owner2,association,instance1,instance2,setName1,setName2,name1,name2)
		String f1=shortName(owner1),f2=shortName(owner2)
		Object a=association."findBy${f1}And${f2}"(instance1,instance2)
		if(!a) {
			a=association.newInstance() // returns null!
			assert a
			instance1?."addTo${f2u(setName1)}"(a)
			instance2?."addTo${f2u(setName2)}"(a)
			assert a.save()
		}
		return a
	}
	static void unlink(Class owner1,Class owner2,Class association,
			Object instance1,Object instance2,
			String setName1=f2l(plural(shortName(association))),String setName2=f2l(plural(shortName(association))),
			String name1=f2l(shortName(owner1)),String name2=f2l(shortName(owner2))) {
		sanityCheck(owner1,owner2,association,instance1,instance2,setName1,setName2,name1,name2)
		String f1=shortName(owner1),f2=shortName(owner2)
		def a=association."findBy${f1}And${f2}"(instance1,instance2)
		if(a) {
			instance1?."removeFrom${f2u(setName1)}"(a)
			instance2?."removeFrom${f2u(setName2)}"(a)
			a.delete()
		}
	}
	public static void main(def args){
	}
}
