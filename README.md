# AWSPlayerBurst

AWSPlayerBurst automatically enables t2/t3.unlimited mode for AWS EC2 hosted servers when players are connected. Once the server is empty, unlimited mode is disabled.

AWSPlayerBurst can drastically decrease the cost of some servers which sometimes sit idle.

This plugin **requires **you to be using Amazon Web Services' EC2 for hosting.

# Setup

1. From the IAM Management Console: Create an IAM role, with the EC2 "common use case" selected ([AWS Support Walkthrough]('https://docs.aws.amazon.com/IAM/latest/UserGuide/id_roles_create_for-service.html#roles-creatingrole-service-console')).
1. Assign the "AmazonEC2FullAccess" permission policy to the role.
1. The role's name, tags, and description can be set to anything.
1. Once created, select the server instance in the EC2 Management Console. In the "Actions" menu, choose "Instance Settings" -> "Attach/Replace IAM Role" ([AWS Support Walkthrough]('https://aws.amazon.com/premiumsupport/knowledge-center/assign-iam-role-ec2-instance/')).
1. Assign the role created in steps 1-3 to the instance. Press apply.
1. Install the plugin .jar file in the server's "plugins" directory.
1. Restart the server.

Confirmation messages should appear in the server's console once running.

# Help/Suggestions

If you experience any issues, or have feedback, please reach out [here]('https://github.com/michaelcerne/AWSPlayerBurst/issues').


##### Licensed under [LGPL-2.1]('https://github.com/michaelcerne/AWSPlayerBurst/blob/master/LICENSE.md').