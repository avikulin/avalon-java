package contracts.businesslogic.analyzers;

import contracts.dataobjects.Statement;

public interface LexicalAnalyzer {
    Statement analyze(int cmdId, String source);
}
