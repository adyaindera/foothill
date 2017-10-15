/**
 * Navigator class to provide navigation feature for the main class,
 * to go back and forward from the stack of links
 *
 * @author Foothill College, Adya Putra Indera
 */
public class Navigator
{
    private String currentLink;
    private StackList<String> backLinks;
    private StackList<String> forwardLinks;

    /**
     * default constructor to set empty current link
     * and empty stack of back links and forward links
     */
    public Navigator()
    {
        this("", new StackList<>("Back", null),
                new StackList<>("Forward", null));
    }

    /**
     * constructor to set link and stack
     *
     * @param currentLink set current link
     * @param backLinks set stack of back links
     * @param forwardLinks set stack of forward links
     */
    public Navigator(String currentLink, StackList<String> backLinks,
                     StackList<String> forwardLinks)
    {
        this.currentLink = currentLink;
        this.backLinks = backLinks;
        this.forwardLinks = forwardLinks;
    }

    /**
     * set the current link, push previous current link to stack of back links,
     * and clear stack forward links
     *
     * @param currentLink link to update
     * @return true/false after checking the condition
     */
    public boolean setCurrentLink(String currentLink)
    {
        if (currentLink == "" || currentLink == null)
            return false;

        if (this.currentLink != null)
            backLinks.push(this.currentLink);

        while (forwardLinks.pop() != null)
            ; // pop until empty

        this.currentLink = currentLink;

        return true;
    }

    /**
     * go back to the top of stack of back links if there is any,
     * if not then it will give warning
     */
    public void goBack()
    {
        if (backLinks.top == null)
        {
            System.out.print("\nWARNING! No back links left.");
            return;
        }

        forwardLinks.push(currentLink);
        currentLink = backLinks.pop();
    }

    /**
     * go forward to the top of stack of forward links if there is any,
     * if not then it will give warning
     */
    public void goForward()
    {
        if (forwardLinks.top == null)
        {
            System.out.print("\nWARNING! No forward links left.");
            return;
        }

        backLinks.push(currentLink);
        currentLink = forwardLinks.pop();
    }

    /**
     * accessor for current link
     *
     * @return the current link
     */
    public String getCurrentLink() { return currentLink; }

    /**
     * accessor for stack of back links
     *
     * @return stack of back links
     */
    public StackList<String> getBackLinks() { return backLinks; }

    /**
     * accessor for stack of forward links
     *
     * @return stack of back links
     */
    public StackList<String> getForwardLinks() { return forwardLinks; }
}
