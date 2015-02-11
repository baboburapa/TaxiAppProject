import java.io.File;
import javax.script.*;

public class ExecuteScript {
	public static void main(String[] args) throws Exception {
		// create a script engine manager
		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		File file = new File("D:/My Program Project/workspace/RTIC-PROJECT/md5.js");
		engine.eval(new java.io.FileReader(file));
	}
}
