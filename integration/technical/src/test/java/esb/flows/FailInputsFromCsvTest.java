package esb.flows;

public class FailInputsFromCsvTest extends ActiveMQTest{
    private String fakeRequeteAvion = "fggfghhgjghjqsdfdfgjhvsdfjhfsqdjhfbhjsbjhdbhjsdfsjhdghjfsbhjdfgjhbdgfjbhgfd" +
            "gjdfhjkgdfghdfjkgjkfdgjkdfgjkdfhdgf" +
            "fdklgdflkgjgjdfghjhjgfdhjkgfd" +
            "flkgdfgjkhfdkhjghjkfd" +
            "dkfgjkfdhgdjkfghjkd";

    //va falloir créer 6 fake request : une avion une car une hotel une manager, une spend et une manager spend comme celle d au dessus
    //ensuite il faut testé que quand tu envois les fake request dans les FILE_INPUT
    //la DEATH_POOL recoit bien le message et que le header du message est bien "failinput" >> exchange.getHeader("err")
    //si t'es perdu regarde le testDeathPoolTest
}
