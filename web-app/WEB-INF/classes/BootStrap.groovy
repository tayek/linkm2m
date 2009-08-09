import org.codehaus.groovy.grails.commons.*
class BootStrap {
	def grailsApplication
	def init = { servletContext ->
		grailsApplication.domainClasses.each { DefaultGrailsDomainClass dc->
			println "dc=${dc}"
			dc.properties.each { DefaultGrailsDomainClassProperty p->
				println "\t${p.name}"
				p.properties.each {
					println "\t\t${it}"
				}
			}
			
		}
	}
	def destroy = {
	}
} 