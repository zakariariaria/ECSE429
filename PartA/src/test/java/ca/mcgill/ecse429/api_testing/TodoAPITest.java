package ca.mcgill.ecse429.api_testing;

import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.Random.class)

public class TodoAPITest {
    
    @Test
    public void test1() {
    	int test = 5;
    	assertEquals(5,test);
    }
    @Test
    public void test2() {
    	int test = 5;
    	assertEquals(5,test);
    }
    @Test
    public void test3() {
    	int test = 5;
    	assertEquals(5,test);
    }
    @Test
    public void test4() {
    	int test = 5;
    	assertEquals(5,test);
    }
    @Test
    public void test5() {
    	int test = 5;
    	assertEquals(5,test);
    }
    
    
    
}