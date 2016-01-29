package numericCommunitiesStats.communitybased

import java.io.FileWriter

/**
 * Created with IntelliJ IDEA.
 * User: padre
 * Date: 14.03.14
 * Time: 18:33
 */
trait Writable {
    protected def saveResult(result: Seq[Any], path: String) {
        val out = new FileWriter(path)
        out.write(result.map(mkString).mkString("\n"))
        out.close()
    }

    protected def mkString(value: Any) = value match{
        case (x, y) => x + " " + y
        case x => x.toString
    }

}
