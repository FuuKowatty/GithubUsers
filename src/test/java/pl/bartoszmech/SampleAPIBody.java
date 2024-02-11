package pl.bartoszmech;

public interface SampleAPIBody {

    default String bodyWithThreeRepositoriesJson() {
        return """
            [
            {
                "name": "Repository1",
                "owner": {
                    "login": "Owner1"
                }
            },
            {
                "name": "Repository2",
                "owner": {
                    "login": "Owner2"
                }
            },
            {
                "name": "Repository3",
                "owner": {
                    "login": "Owner3"
                }
            }
            ]
            """.trim();
    }

    default String bodyWithBranchesJson(String[] branches) {
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < branches.length; i++) {
            jsonBuilder.append("{\"name\":\"").append(branches[i]).append("\",")
                .append("\"commit\":{\"sha\":\"").append("sha").append(i).append("\"}}");
            if (i < branches.length - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }

}
