package link

class Vip extends Person {
	int money=1000
    static constraints = {
		money(min:1000)
	}
}

