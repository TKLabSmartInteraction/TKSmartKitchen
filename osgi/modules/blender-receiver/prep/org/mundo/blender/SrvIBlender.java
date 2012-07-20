package org.mundo.blender;


/**
 * Automatically generated server stub for <code>IBlender</code>
 * @see org.mundo.blender.IBlender
 */
public class SrvIBlender extends org.mundo.rt.ServerStub
{
  public SrvIBlender()
  {
  }
  private static org.mundo.rt.ServerStub _obj;
  public static org.mundo.rt.ServerStub _getObject()
  {
    if (_obj==null)
    {
      _obj=new SrvIBlender();
    }
    return _obj;
  }
  public void invoke(Object o, org.mundo.rt.TypedMap m, org.mundo.rt.TypedMap r)
  {
    String n=m.getString("request");
    IBlender p=(IBlender)o;
    try
    {
      if (n.equals("pushButton") && m.getString("ptypes").equals("i"))
      {
        r.putBoolean("value", p.pushButton(m.getInt("p0")));
        return;
      }
      if (n.equals("pushButton") && m.getString("ptypes").equals("i,i"))
      {
        r.putBoolean("value", p.pushButton(m.getInt("p0"), m.getInt("p1")));
        return;
      }
      if (n.equals("lightOn") && m.getString("ptypes").equals("t"))
      {
        r.putBoolean("value", p.lightOn(m.getBoolean("p0")));
        return;
      }
      if (n.equals("_getMethods") && m.getString("ptypes").equals(""))
      {
        r.putString("value",
        "t pushButton(i)\n"+
        "t pushButton(i,i)\n"+
        "t lightOn(t)\n"+
        "");
        return;
      }
    }
    catch(Exception x)
    {
      exceptionOccured(x, o, m, r);
    }
  }
}