package util
import org.codehaus.groovy.grails.commons.*
public class Linkm2m {
	Linkm2m(final Class owner1,final Class owner2,final Class association) {
		this(owner1,owner2,association,f2l(plural(shortName(association))),f2l(plural(shortName(association))))
	}
	Linkm2m(final Class owner1,final Class owner2,final Class association,
	final String setName1,final String setName2) {
		this(owner1,owner2,association,setName1,setName2,f2l(shortName(owner1)),f2l(shortName(owner2)))
	}
	Linkm2m(final Class owner1,final Class owner2,final Class association,
	//the stuff below is broken in current versions of groovy, hence these constructors
	//final String setName1=f2l(plural(shortName(association))),final String setName2=f2l(plural(shortName(association))),
	//final String name1=f2l(shortName(owner1)),final String name2=f2l(shortName(owner2))) {
	final String setName1,final String setName2,
	final String name1,final String name2) {
		assert owner1
		assert owner2
		assert association
		this.owner1=owner1
		this.owner2=owner2
		this.association=association
		this.setName1=setName1
		this.setName2=setName2
		this.name1=name1
		this.name2=name2
		findBy="findBy${f2u(name1)}And${f2u(name2)}"
		addTo1="addTo${f2u(setName1)}"
		addTo2="addTo${f2u(setName2)}"
		removeFrom1="removeFrom${f2u(setName1)}"
		removeFrom2="removeFrom${f2u(setName2)}"
		sanityCheck()
	}
	Object link(final Object instance1,final Object instance2) {
		link(instance1,instance2,null)
	}
	Object link(final Object instance1,final Object instance2,final Closure closure) {
		assert owner1.isInstance(instance1)
		assert owner2.isInstance(instance2)
		def params = [instance1,instance2] as Object[]
		Object a=association.invokeMethod(findBy,params)
		if(!a) {
			a=association.newInstance()
			assert a
			params=[a] as Object[]
			instance1?.invokeMethod(addTo1,params)
			instance2?.invokeMethod(addTo2,params)
			if(closure)
				closure(a)
			assert a.save()
		}
		return a
	}
	Object unlink(final Object instance1,final Object instance2) {
		unlink(instance1,instance2,null)
	}
	void unlink(final Object instance1,final Object instance2,Closure closure) {
		assert owner1.isInstance(instance1)
		assert owner2.isInstance(instance2)
		def params = [instance1,instance2] as Object[]
		Object a=association.invokeMethod(findBy,params)
		if(a) {
			params=[a] as Object[]
			instance1?.invokeMethod(removeFrom1,params)
			instance2?.invokeMethod(removeFrom2,params)
			if(closure)
				closure(a)
			a.delete()
		}
	}
	protected void sanityCheck() {
		final DefaultGrailsDomainClass owner1Domain=new DefaultGrailsDomainClass(owner1)
		final GrailsDomainClassProperty p1=owner1Domain.getPropertyByName(setName1)
		assert p1
		assert p1.oneToMany
		assert p1.bidirectional
		assert p1.referencedPropertyType==association
		assert p1.referencedPropertyName==name1
		final DefaultGrailsDomainClass owner2Domain=new DefaultGrailsDomainClass(owner2)
		final GrailsDomainClassProperty p2=owner2Domain.getPropertyByName(setName2)
		assert p2
		assert p2.oneToMany
		assert p2.bidirectional
		assert p2.referencedPropertyType==association
		assert p2.referencedPropertyName==name2
		final DefaultGrailsDomainClass associationDomain=new DefaultGrailsDomainClass(association)
		final GrailsDomainClassProperty p3=associationDomain.getPropertyByName(name1)
		assert p3
		assert p3.manyToOne
		assert p3.bidirectional
		assert p3.referencedPropertyType==owner1
		assert p3.referencedPropertyName==setName1
		final GrailsDomainClassProperty p4=associationDomain.getPropertyByName(name2)
		assert p4
		assert p4.manyToOne
		assert p4.bidirectional
		assert p4.referencedPropertyType==owner2
		assert p4.referencedPropertyName==setName2
	}
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
	private final Class owner1,owner2,association
	private final String setName1,setName2
	private final String name1,name2
	private final String findBy,addTo1,addTo2,removeFrom1,removeFrom2
}
