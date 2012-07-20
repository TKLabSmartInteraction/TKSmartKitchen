package org.mundo.blender;


/**
 * Automatically generated server stub for <code>IBlenderEvent</code>
 * @see org.mundo.blender.IBlenderEvent
 */
public class SrvIBlenderEvent extends org.mundo.rt.ServerStub
{
  public SrvIBlenderEvent()
  {
  }
  private static org.mundo.rt.ServerStub _obj;
  public static org.mundo.rt.ServerStub _getObject()
  {
    if (_obj==null)
    {
      _obj=new SrvIBlenderEvent();
    }
    return _obj;
  }
  public void invoke(Object o, org.mundo.rt.TypedMap m, org.mundo.rt.TypedMap r)
  {
    String n=m.getString("request");
    IBlenderEvent p=(IBlenderEvent)o;
    try
    {
      if (n.equals("buttonPushed") && m.getString("ptypes").equals("i"))
      {
        p.buttonPushed(m.getInt("p0"));
        return;
      }
      if (n.equals("buttonReleased") && m.getString("ptypes").equals("i"))
      {
        p.buttonReleased(m.getInt("p0"));
        return;
      }
      if (n.equals("_getMethods") && m.getString("ptypes").equals(""))
      {
        r.putString("value",
        "v buttonPushed(i)\n"+
        "v buttonReleased(i)\n"+
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